def map_break_message(b):
    '''
    given integer b representing break message
    returns appropriate string representing the type of break given (if any)
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


def take_break(s, reward_break=True, acc_min_change=.2, time_min_change=10, t=4, refractory_period=4, max_study_time=15):
    '''
    Intended for determination of breaks for reward and frustration break scenarios
    (expGroup == 2 or expGroup == 3)
    Parameters:
        s: session object
        reward_break: True if reward breaks, False if frustration breaks
        t: int, consistency value
        refractory_period: int, for super rule 2 (no break if less than refractory_period questions answered since last break)
        max_study_time: int, for super rule 3 (take break if no break has been taken in last max_study_time minutes)
    returns (boolean, string)
        boolean: represents whether or not to trigger a break
        string: message representing reasoning for break
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
                (break_trigger, break_val) = check_consistency(s, reward_break, t)
        else:  # overall accuracy < 80%
            if time_change > 0:  # time slower
                break_trigger = True
                break_val = 6
            elif time_change < 0:  # time faster
                break_trigger = True
                break_val = 9
            else:  # no change
                (break_trigger, break_val) = check_consistency(s, reward_break, t)

    # break_trigger depends on whether or not reward_break:
    if reward_break:  # reward
        if break_val > 4:  # i.e. a break that only occurs on frustration
            break_trigger = False
    else:  # frustration
        if break_val <= 4:  # i.e. a break that only occurs on reward
            break_trigger = False

    # super rule #2: no break if < 4 questions answered since last break
    if break_trigger:  # if break will be taken...
        break_trigger = super_rule2(s, refractory_period=refractory_period)
        b_super = 2

    # super rule #3: take break if no break has been taken in last 15 minutes
    if not break_trigger:
        break_trigger = super_rule3(s, max_study_time=max_study_time)
        b_super = 3

    # finally, insert this break into session object
    # DANGER: this is an important implementation detail!
    # note, b_type could be of type that expects a break_trigger, except break_trigger might
    # be inconsistent because it depends on reward_break type!
    s.insert_break(b_type=break_val, b_super=b_super, triggered_break=break_trigger)

    return (break_trigger, map_break_message(break_val))


def super_rule3(s, max_study_time=15):
    '''
    returns True if break must be taken because of super rule 3, False otherwise (i.e. break could be taken, maybe)
    '''
    max_time_ms = max_study_time*60000  # convert to ms
    current_time_since_start_ms = s.time_step()

    # check if there has been break in last max_time_ms
    breaked_recently = False
    for b in reversed(s.breaks):
        if current_time_since_start_ms - b.time_since_start < max_time_ms:
            if b.triggered_break:
                breaked_recently = True
        else:  # breaks too far in past to count
            breaked_recently = False
            break

    return not breaked_recently


def super_rule2(s, refractory_period=4):
    '''
    returns True if break_trigger could be true, False if it fails this rule
    '''
    num_questions_since_last_break = 0
    for b in reversed(s.breaks):
        if b.triggered_break:
            break
        else:
            num_questions_since_last_break += 1

    return (num_questions_since_last_break >= refractory_period)


def check_consistency(s, reward_break, t=4):
    '''
    checks consistency condition for session, given a
        reward_break: boolean, True if reward break, False otherwise
        t: integer representing how many questions should be evaluated "no change" in a row before consistency break given
    Returns (boolean, int)
        boolean: break_trigger, true if break should be triggered, false otherwise
        int: break_val, corresponds to appropriate string in map_break_message
    '''
    break_trigger = False
    break_val = -1
    b_val_no_change = -1

    if reward_break:
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
        if reward_break:  # means reward situation
            break_val = 3
        else:
            break_val = 8
    elif in_a_row < t:
        break_trigger = False
        if reward_break:
            break_val = 4
        else:
            break_val = 7
    else:  # should never get here!
        print 'error in check_consistency: logic'

    return (break_trigger, break_val)


def calc_time_change(s, min_change=10):
    '''
    Calculates whether or not time has increased, decreased, or no change
    Parameters:
        s: session object
        min_change: float representing minimum change needed for accuracy change to count as 'increasing' or 'decreasing'
    Returns -1 if decreased (faster), 0 if no change, and 1 if increased (slower)
    '''

    current_window_avg_time = s.calc_window_avg_time(offset=0)
    prev_window_avg_time = s.calc_window_avg_time(offset=1)

    if current_window_avg_time > prev_window_avg_time + abs(min_change):
        return 1
    elif current_window_avg_time < prev_window_avg_time - abs(min_change):
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
    prev_window_accuracy = s.calc_window_accuracy(offset=1)

    if current_window_accuracy > prev_window_accuracy + abs(min_change):  # check increasing condition
        return 1
    elif current_window_accuracy < prev_window_accuracy - abs(min_change):  # check decreasing condition
        return -1
    else:  # no change
        return 0
