from num2words import num2words
import random 
import json
import pprint
# for questionID
counter = 220

#list for all questions to be added to
total = []
#multiplication problems
for problem in range(50):
	question = {'QuestionID': counter}
	counter += 1
	s1 = random.randint(0,9)
	a1 = random.randint(5,9)
	b1 = random.randint(0,4)
	a2 = random.randint(5,9)
	b2 = random.randint(0,4)
	# want subtraction
	if (a1 * b1) > (a2 * b2):
		question['Question'] = "What is %d + %d X %d - %d X %d?" % (s1, a1, b1, a2, b2)
		question["Spoken Question"] = "What is %s plus %s times %s minus %s times %s?" % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = s1 + (a1 *b1) - (a2 * b2)
		question["Answer" ]	= (answer)
		question["Spoken Answer"] = "The correct answer is %s." %(num2words(answer))
		question["Most Common Mistake"] = ((s1 + a1) * b1 - (a2 * b2))
	# want addition
	'''
	else:
		print '	"Question" : "What is %d + %d X %d + %d X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is %s plus %s times %s plus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = s1 + (a1 *b1) + (a2 * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		print '	"Most Common Mistake" : %d,' % ((s1 + a1) * b1 + (a2 * b2))
	print '	"Difficulty Level" : 2,'
	print '	"Problem Type" : "Multiplication", '
	print '	"Max Time" : 60'
	'''
	print(json.dumps(question))


#print total