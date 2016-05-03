def take_break(s, reward_break=True):
    '''
    given session object s

    returns boolean representing whether or not to trigger a break
    '''
    return True
    if len(s) % 2 == 1:
        return True
    else:
        return False
