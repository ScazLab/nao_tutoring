

def take_break(s, reward_break=True):
    '''
    Intended for determination of breaks for reward and frustration break scenarios
    (expGroup == 2 or expGroup == 3)
    Parameters:
        s: session object
        reward_break: True if reward breaks, False if frustration breaks

    returns boolean representing whether or not to trigger a break
    '''
    accuracy_change = calc_accuracy_change(s)  # -1 if decrease, 0 if no change, 1 if increased
    time_change = calc_time_change(s)  # -1 if decrease, 0 if no change, 1 if increased

    current_window_avg_time = s.calc_window_avg_time(offset=0)
    prev_window_avg_time = s.calc_window_avg_time(offset=1)

    if current_window_accuracy



    return True
    if len(s) % 2 == 1:
        return True
    else:
        return False

def calc_accuracy_change(s, min_change=.2):
    '''
    Calculates whether or not accuracy has increased, decreased, or no calc_accuracy_change
    Parameters:
        s: session object
        min_change: float representing minimum change needed for accuracy change to count as 'increasing' or 'decreasing'
    Returns -1 if decreased, 0 if no change, and 1 if increased
    '''
    current_window_accuracy = s.calc_window_accuracy(offset=0)
    prev_window_accuracy = s.calc_window_accuracy(offset=1)

    if current_window_accuracy > prev_window_accuracy + abs(min_change):  # check increasing condition
        return 1
    elif current_window_accuracy < prev_window_accuracy - abs(min_change):  # check decreasing condition
        return -1
    else:  # no change
        return 0
