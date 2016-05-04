

def map_break_message(b):
    '''
    given integer b representing break message
    returns appropriate string representing the type of break given (if any)
    '''
    dic = {
        0: "overcomes struggle, improves",
        1: "improves, takes time",
        2: "becoming faster/more confident",
        5: "bored/distracted/disengaged",
        6: "disengaged",
        9: "guessing, giving up",
        10: "performance drop",
        11: "guessing, making mistakes",
    }

    return dic[b]


def take_break(s, reward_break=True):
    '''
    Intended for determination of breaks for reward and frustration break scenarios
    (expGroup == 2 or expGroup == 3)
    Parameters:
        s: session object
        reward_break: True if reward breaks, False if frustration breaks

    returns (boolean, string)
        boolean: represents whether or not to trigger a break
        string: message representing reasoning for break
    '''
    break_trigger = False
    break_val = 0
    total_accuracy = s.calc_total_accuracy()
    accuracy_change = s.calc_accuracy_change()  # -1 if decrease, 0 if no change, 1 if increased
    time_change = calc_time_change(s)  # -1 if decrease, 0 if no change, 1 if increased

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
                (break_trigger, break_val) = check_consistency(s, reward_break)
                # consistency stuff
                pass
        else:  # overall accuracy < 80%
            if time_change > 0:  # time slower
                break_trigger = True
                break_val = 6
            elif time_change < 0:  # time faster
                break_trigger = True
                break_val = 9
            else:  # no change
                # consistency stuff
                pass

    # finally, insert this break into session object
    s.insert_break(b_type=break_val, triggered_break=break_trigger)
    return (break_trigger, map_break_message(break_val))




    return True
    if len(s) % 2 == 1:
        return True
    else:
        return False


def check_consistency(s, reward_break):
    '''
    checks consistency condition for session, given a
        reward_break: boolean, True if reward break, False otherwise

    Returns (boolean, int)
        boolean: break_trigger, true if break should be triggered, false otherwise
        int: break_val, corresponds to appropriate string in map_break_message
    '''
    break_trigger = False
    break_val = -1


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
