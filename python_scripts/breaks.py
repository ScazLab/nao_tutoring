def take_break(s):
    '''
    given session object s

    returns boolean representing whether or not to trigger a break
    '''
    if len(s) % 4 == 1:
        return True
    else:
        return False
