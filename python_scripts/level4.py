from num2words import num2words
import random 

#old level3 parentheses questions, put here as level4

counter = 420
print "["

# for parentheses problems
for problem in range(50):
	s1 = random.randint(10,99)
	a1 = random.randint(5,9)
	b1 = random.randint(5,9)
	a2 = random.randint(5,9)
	b2 = random.randint(0,4)
	print ' {'
	print '	"QuestionID" : %d,' % counter
	counter = counter + 1
	# want subtraction
	if counter % 2 == 0:

		print '	"Question" : "What is (%d + %d) X %d - %d X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is (%s plus %s) times %s minus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = (s1 + a1) *b1 - (a2 * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		print '	"Most Common Mistake" : %d,' % (s1 + (a1 * b1) - (a2 * b2))
	# want addition
	elif counter % 2 == 1:
		print '	"Question" : "What is %d + %d X (%d + %d) X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is %s plus %s times (%s plus %s) times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = s1 + (a1 *(b1 + a2) * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		print '	"Most Common Mistake" : %d,' % ((s1 + a1) * b1 + (a2 * b2))
	print '	"Difficulty Level" : 4,'
	print '	"Problem Type" : "Parentheses", '
	print '	"Max Time" : 60'
	print ' },'
	print ""
print "]"

