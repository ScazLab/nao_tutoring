import random

def map_break_message(b):
    '''
    [deprecated]
    A method based on preliminary break messages in break decision tree
    https://docs.google.com/presentation/d/1zC5jF_YhU6bmlgypRAwLjTgIELat0LAns3Q02QWbwBM/edit#slide=id.g12565d9b4e_0_73

    Parameters:
        b: integer representing break message type

    Returns appropriate string representing the type of break given (if any)
    '''
    dic = {
        0: "overcomes struggle, improves",  # REWARD start
        1: "improves, takes time",
        2: "becoming faster/more confident",
        3: "doing consistently well",
        4: "not consistent for long enough (reward)",  # note: no break!
        5: "bored/distracted/disengaged",  # FRUSTRATION start
        6: "disengaged",
        7: "not consistent for long enough (frustration)",  # note: no break!
        8: "doing consistently poorly, frustrated",
        9: "guessing, giving up",
        10: "performance drop",
        11: "guessing, making mistakes",
    }

    return dic[b]


def take_break(s, reward_break=True, acc_min_change=.05, time_min_change=3, t=4, refractory_period=4, max_study_time=15):
    '''
    Method intended for determination of breaks for reward and frustration break scenarios

    Parameters:
        s: session object
        reward_break: boolean.  True if reward breaks, False if frustration breaks
        acc_min_change: Float represneting min accuracy change needed to trigger increase/decrease condition
        time_min_change: float representing min time change needed to trigger increase/decrease condition
        t: int, consistency constant
            i.e. number of questions answered previously 'consistently' before consistency break can be triggered
        refractory_period: int, for super rule 2 (no break if less than refractory_period questions answered since last break)
        max_study_time: int, for super rule 3 (take break if no break has been taken in last max_study_time minutes)

    Returns (boolean, string)
        boolean: represents whether or not to trigger a break
        string: message representing reasoning for break based on map_break_message [deprecated]
    '''
    break_trigger = False
    break_val = 0
    total_accuracy = s.calc_total_accuracy()
    accuracy_change = calc_accuracy_change(s, min_change=acc_min_change)  # -1 if decrease, 0 if no change, 1 if increased
    time_change = calc_time_change(s, min_change=time_min_change)  # -1 if decrease, 0 if no change, 1 if increased
    b_super = -1

    if accuracy_change > 0:  # accuracy increase
        if time_change <= 0:  # time faster or no change
            break_trigger = True
            break_val = 0
        else:  # time slower
            break_trigger = True
            break_val = 1
    elif accuracy_change < 0:  # accuracy decrease
        if time_change >= 0:  # time slower or no change
            break_trigger = True
            break_val = 10
        else:  # time faster
            break_trigger = True
            break_val = 11
    else:  # no accuracy change
        if total_accuracy >= .8:  # overall accuracy >= 80%
            if time_change < 0:  # time faster
                break_trigger = True
                break_val = 2
            elif time_change > 0:  # time slower
                break_trigger = True
                break_val = 5
            else:  # no change
                (break_trigger, break_val) = check_consistency(s, reward_break=reward_break, acc_high=True, t=4)
        else:  # overall accuracy < 80%
            if time_change > 0:  # time slower
                break_trigger = True
                break_val = 6
            elif time_change < 0:  # time faster
                break_trigger = True
                break_val = 9
            else:  # no change
                (break_trigger, break_val) = check_consistency(s, reward_break=reward_break, acc_high=False, t=4)

    # break_trigger depends on whether or not reward_break:
    if reward_break:  # reward
        if break_val > 4:  # i.e. a break that only occurs on frustration
            break_trigger = False
    else:  # frustration
        if break_val <= 4:  # i.e. a break that only occurs on reward
            break_trigger = False

    # super rule #3: take break if no break has been taken in last 15 minutes
    if not break_trigger:
        break_trigger = super_rule3(s, max_study_time=max_study_time)
        if break_trigger:  # mark when superrule3 makes a break
            b_super = 3

    # super rule #2: no break if < 4 questions answered since last break
    if break_trigger:  # if break will be taken...
        break_trigger = super_rule2(s, refractory_period=refractory_period)
        if not break_trigger:  # mark when superrule2 overrides a break
            b_super = 2

    # finally, insert this break into session object
    s.insert_break(b_type=break_val, b_super=b_super, triggered_break=break_trigger)

    return (break_trigger, map_break_message(break_val))


def super_rule3(s, max_study_time=15):
    '''
    Parameters:
        max_study_time: int representing max study time in minutes

    Returns True if break must be taken because of super rule 3, False otherwise (i.e. break could be taken, maybe)
    '''
    max_time_ms = max_study_time*60000  # convert to ms
    current_time_since_start_ms = s.time_step()
    first_block_of_time = current_time_since_start_ms <= max_time_ms # True if first block of time and shouldn't take break

    # check if there has been break in last max_time_ms
    breaked_recently = False
    for b in reversed(s.breaks):
        if current_time_since_start_ms - b.time_since_start < max_time_ms:
            if b.triggered_break:
                breaked_recently = True
        else:  # breaks too far in past to count
            breaked_recently = False
            break

    if first_block_of_time:  # do not take break if still in first block of time
        return False

    return not breaked_recently


def super_rule2(s, refractory_period=4):
    '''
    Parameters:
        refractory_period: int representing number of questions needed before another break allowed to be served
    
    Returns True if break_trigger could be true, False if it fails this rule
    '''
    num_questions_since_last_break = 0
    for b in reversed(s.breaks):
        if b.triggered_break:
            break
        else:
            num_questions_since_last_break += 1

    return (num_questions_since_last_break >= refractory_period)


def check_consistency(s, reward_break, acc_high, t=4):
    '''
    Checks consistency condition for session, given the following properties

    Parameters:
        reward_break [deprecated, not used]: boolean, True if reward break, False otherwise
            DANGER: currently not used in method
        t: integer representing how many questions should be evaluated "no change" in a row before consistency break given
        acc_high: boolean, True if accuracy high condition, False otherwise
    
    Returns (boolean, int)
        boolean: break_trigger, true if break should be triggered, false otherwise
        int: break_val, corresponds to appropriate string in map_break_message
    '''
    break_trigger = False
    break_val = -1
    b_val_no_change = -1

    if acc_high:
        b_val_no_change = 4
    else:
        b_val_no_change = 7

    in_a_row = 0
    for b in reversed(s.breaks):
        if b.b_type == b_val_no_change:  # no change in time yet <t (i.e. no break triggered in sequence)
            in_a_row += 1
        else:  # otherwise, stop counting
            break

    if in_a_row == t:  # means time to trigger break!
        break_trigger = True
        if acc_high:  # means accuracy high tree situation
            break_val = 3
        else:
            break_val = 8
    elif in_a_row < t:
        break_trigger = False
        if acc_high:
            break_val = 4
        else:
            break_val = 7
    else:  # should never get here!
        print 'error in check_consistency: logic'

    return (break_trigger, break_val)


def calc_time_change(s, min_change=1):
    '''
    Calculates whether or not time has increased, decreased, or no change

    Parameters:
        s: session object
        min_change: float representing minimum change needed for accuracy change to count as 'increasing' or 'decreasing'

    Returns -1 if decreased (faster), 0 if no change, and 1 if increased (slower)
    '''

    current_window_avg_time = s.calc_window_avg_time(offset=0)
    total_window_avg_time = s.calc_total_avg_time()

    print "current_window_avg_time: " + str(current_window_avg_time)
    print "total_window_avg_time: " + str(total_window_avg_time)

    if current_window_avg_time > total_window_avg_time + abs(min_change):
        return 1
    elif current_window_avg_time < total_window_avg_time - abs(min_change):
        return -1
    else:
        return 0


def calc_accuracy_change(s, min_change=.2):
    '''
    Calculates whether or not accuracy has increased, decreased, or no change

    Parameters:
        s: session object
        min_change: float representing minimum change needed for accuracy change to count as 'increasing' or 'decreasing'

    Returns -1 if decreased (less accurate), 0 if no change, and 1 if increased (more accurate)
    '''
    current_window_accuracy = s.calc_window_accuracy(offset=0)
    total_window_accuracy = s.calc_total_accuracy()

    print "current_window_accuracy: " + str(current_window_accuracy)
    print "total_window_accuracy: " + str(total_window_accuracy)

    if current_window_accuracy > total_window_accuracy + abs(min_change):  # check increasing condition
        return 1
    elif current_window_accuracy < total_window_accuracy - abs(min_change):  # check decreasing condition
        return -1
    else:  # no change
        return 0


BREAK_SPEECH = {
    "fixed": [ "Since it has been a few minutes, lets take a break.",
               "I think it is time for a break." ],
    "base-rules": {
        # Reward breaks
        0: [ "Wow! Looks like you're really improving! Time for a little activity and then we'll get back to it.",
             "You've really improved! You deserve a break." ],
        1: [ "I think you're really trying and improving! How about we do a quick activity and then keep going!" ],
        2: [ "You're getting the problems even faster, good job! Let's take a quick break and then do some more problems!" ],
        3: [ "Wow, you've been doing great for a while now! How about a quick activity?" ],

        # Frustration breaks
        5:  [ "bored/distracted/disengaged" ],
        6:  [ "disengaged" ],
        8:  [ "doing consistently poorly, frustrated" ],
        9:  [ "guessing, giving up" ],
        10: [ "Hmm, why don't we take a little break to refocus and then come back to our problems!" ],
        11: [ "guessing, making mistakes" ],
    },
    "super-rules": {
        3: [ "long time no breaks" ]
    }
}


def get_break_speech(exp_group, b_super, b_type):
    # If fixed condition
    if exp_group == 1:
        return random.choice(BREAK_SPEECH["fixed"])

    # If reward or frustration condition
    if b_super in BREAK_SPEECH["super-rules"]:
        return random.choice(BREAK_SPEECH["super-rules"][b_super])
    if b_type in BREAK_SPEECH["base-rules"]:
        return random.choice(BREAK_SPEECH["base-rules"][b_type])

    return str()
