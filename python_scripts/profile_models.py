import time
import pprint


class Session(list):
    '''
    Note: to access questions, call Session object like a list: a session IS a list of questions
    A session is a list of questions with the following properties:

    Properties:
        session_num: Integer representing session number (or expGroup in break tutoring)
        pid: Integer representing user pid
        time_window: int representing window size for time calculations
        accuracy_window: int representing window size for accuracy calculations
        breaks: list of break objects representing break decisions taken in this session
            each break is associated with one question
            thus, the 'ith' question of session is associated with the 'ith' break in breaks
    '''

    def __init__(self, questions=None, session_num=-1, pid=-1, time_window=5, accuracy_window=5, breaks=None):
        self.session_num = session_num
        self.pid = pid
        self.time_window = time_window
        self.accuracy_window = accuracy_window
        self.breaks = breaks

        if questions is None:
            super(Session, self).__init__([])
        else:
            super(Session, self).__init__(questions)

        if breaks is None:
            self.breaks = []
        else:
            self.breaks = breaks

        # private vars for internal use
        self.__start_time = time.time()
        self.__now_time = time.time()

    def time_step(self):
        '''
        Updates __now_time

        Returns elapsed time since session creation 
            i.e. Float representing (ms) difference between __now_time and __start_time
        '''
        self.__now_time = time.time()
        return self.__now_time - self.__start_time

    def insert_break(self, b_type, b_super, triggered_break):
        '''
        Inserts break into breaks (list) of session object

        Returns void
        '''
        self.breaks.append(Break(b_num=len(self.breaks), after_question=self[-1].question_num, b_type=b_type, b_super=b_super, time_since_start=self.time_step(), triggered_break=triggered_break))
        return

    def calc_total_accuracy(self):
        '''
        Returns total_accuracy: float from 0-1 representing total accuracy ratio
        '''
        total_correct = len([q for q in self if q.answer_correct])
        return 1.0*total_correct/len(self)

    def calc_window_accuracy(self, offset=0):
        '''
        Parameter:
            offset: int representing offset from end that should be used for window
                offset 0 gets most recent window, anything less gets windows starting offset behind most recent window
                i.e. offset 1 gets previous window value

        Returns window_accuracy: float from 0-1 representing most recent window accuracy ratio
        '''
        l = len(self)
        total_correct = len([q for i, q in enumerate(self) if (q.answer_correct and i+self.accuracy_window+offset >= l and i+self.accuracy_window+offset < l+self.accuracy_window)])

        real_window = min(l-offset, self.accuracy_window)

        if real_window <= 0:
            return 0.0

        return 1.0*total_correct/real_window

    def calc_total_avg_time(self):
        '''
        Returns total_avg_time: float representing total average time in seconds
        '''
        total_time = sum([q.total_time for q in self])
        return 1.0*total_time/len(self)

    def calc_window_avg_time(self, offset=0):
        '''
        Property:
            offset: int representing offset from end that should be used for window
                offset 0 gets most recent time_window, anything less gets time_windows starting offset behind most recent
                i.e. offset 1 gets previous time_window value
        Returns window_avg_time: float representing most recent time_window average time in seconds

        '''
        l = len(self)
        total_time = sum([q.total_time for i, q in enumerate(self) if i+self.time_window+offset >= l and i+self.time_window+offset < l+self.time_window])

        real_window = min(l-offset, self.time_window)

        if real_window <= 0:
            return 0.0

        return 1.0*total_time/real_window

    def __repr__(self):
        return "Session(pid=%r, session_num=%r, questions=\\\n%s, breaks=\\\n%s)" % \
            (self.pid, self.session_num, pprint.pformat(list(self)), pprint.pformat(list(self.breaks)))


class Break(object):
    '''
    Note: Break object does not necessarily correspond to triggered breaks.
        Rather, break objects correspond to break decisions, which are made after each question
        Thus, each break has one associated question that it comes after

    Break object representing a break decision with the following properties:

    Properties:
        b_num: int representing 0-indexed break number (subsequent breaks increase b_num chronologically)
        after_question: int representing question that break
        b_type: int representing break type that can be mapped to a reason from map_break_message in breaks.py
            Note: A b_type that normally triggers a break does not necessarily mean a break was given.
                the logic behind break triggers is elaborated upon in take_break of breaks.py
        b_super: int representing break super rule that was used.  defaults at -1 if no super_rule used
        time_since_start: float representing in seconds the time since start that break decision was made
        triggered_break: boolean representing whether or not a break was triggered
    '''
    def __init__(self, b_num=-1, after_question=-1, b_type=-1, b_super=-1, time_since_start=0.0, triggered_break=False):
        self.b_num = b_num
        self.after_question = after_question
        self.b_type = b_type
        self.time_since_start = time_since_start
        self.triggered_break = triggered_break
        self.b_super = b_super

    def __repr__(self):
        return "Break(b_num=%r, after_question=%r, b_type=%r, b_super=%r, time_since_start=%r, triggered_break=%r)" % \
            (self.b_num, self.after_question, self.b_type, self.b_super, self.time_since_start, self.triggered_break)


class Question(object):
    '''
    A Question contains live information about question being answered

    Properties:
        question_num: Integer representing question number in current session
        attempts: Integer representing number of attempts made at question (max 5)
        hints: Integer representing number of hints given (max 3)
        answer_correct: Boolean representing whether or not question was eventually answered answer_correctly
        total_time: Float representing total time, in ms, that was spent on question
        hint_times [deprecated]: Array of Floats representing time (from start) when hint was given
            Note: -1 means that hint was not given
            [24.3, 59.6, -1]:
                hint1 given 24.3 ms after start
                hint2 given 59.6 ms after start
                hint3 not given
        attempt_times [deprecated]: Array of Floats representing time (from start) when attempt was made
            Note: -1 means that attempt was never made
            [35.4, 75.1, -1, -1, -1]:
                attempt1 made 35.4 ms after start
                attempt2 made 75.1 ms after start
                attempt3/4/5 never made
        q_complete: Bool representing whether or not quesiton is complete
        q_timeout: Bool representing that quesiton timed out
    '''
    MAX_HINTS = 3
    MAX_ATTEMPTS = 5

    def __init__(self, question_num=-1, attempts=0, hints=0, answer_correct=False,
                 total_time=0.0, hint_times=None, attempt_times=None, q_complete=False, q_timeout=False):
        self.question_num = question_num
        self.attempts = attempts
        self.hints = hints
        self.answer_correct = answer_correct
        self.total_time = total_time
        self.hint_times = hint_times
        self.attempt_times = attempt_times
        self.q_complete = q_complete
        self.q_timeout = q_timeout

        if hint_times is None:
            self.hint_times = []
        else:
            self.hint_times = hint_times

        if attempt_times is None:
            self.attempt_times = []
        else:
            self.hint_times = hint_times

        # private vars for internal use
        self.__start_time = time.time()
        self.__now_time = time.time()

    def timeout(self, ms_android_time=None):
        '''
        Handles case when question times out
        '''
        time_diff = -1.0
        if ms_android_time:
            time_diff = ms_android_time/1000.0  # convert to seconds
        else:
            time_diff = self.time_step()

        self.total_time = time_diff
        self.answer_correct = False  # just to make sure

        self.q_timeout = True
        self.complete()

    def correct(self, ms_android_time=None):
        '''
        Handles case when question answered correctly
        '''
        time_diff = -1.0
        if ms_android_time:
            time_diff = ms_android_time/1000.0  # convert to seconds
        else:
            time_diff = self.time_step()

        self.attempt_times.append(time_diff)
        self.attempts += 1

        self.total_time = time_diff
        self.answer_correct = True

        self.complete()

    def incorrect(self, ms_android_time=None, last=False):
        '''
        Handles case when question answered incorrectly

        Parameters:
            last: Boolean representing whether or not this is the "last" incorrect allowed
        '''
        time_diff = -1.0
        if ms_android_time:
            time_diff = ms_android_time/1000.0  # convert to seconds
        else:
            time_diff = self.time_step()

        self.attempt_times.append(time_diff)
        self.attempts += 1

        if (last):
            self.total_time = time_diff
            self.answer_correct = False  # just to make sure

            self.complete()

    def hint(self):
        '''
        Handles case when hint is requested
        '''

        time_diff = self.time_step()

        self.hint_times.append(time_diff)
        self.hints += 1

    def time_step(self):
        '''
        Updates __now_time
        Returns Float representing (ms) difference between __now_time and __start_time
        '''
        self.__now_time = time.time()
        return self.__now_time - self.__start_time

    def complete(self):
        '''
        Called when question is complete to perform cleanup operations
        '''

        while len(self.hint_times) < self.MAX_HINTS:
            self.hint_times.append(-1)

        while len(self.attempt_times) < self.MAX_ATTEMPTS:
            self.attempt_times.append(-1)

        self.q_complete = True

    def __repr__(self):
        return "Question(q_num=%r, attempts=%r, hints=%r, answer_correct=%r, total_time=%r, q_complete=%r, q_timeout=%r, \\\nhint_times=%s, attempt_times=%s)" % \
            (self.question_num, self.attempts, self.hints, self.answer_correct, self.total_time, self.q_complete, self.q_timeout,
             pprint.pformat(list(self.hint_times)), pprint.pformat(list(self.attempt_times)))
