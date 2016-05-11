from num2words import num2words
import random 

# initialize array of random order for numbers
# obviously, very unefficient, but for just 1 - 10, seems okay
first = [5, 8, 2, 9, 1, 3, 4, 0, 6, 8, 7]
second = [ 7, 5, 9, 2, 4, 6, 1, 8, 0, 3]
counter = 220
print "["
for problem in range(50):
	s1 = random.randint(0,9)
	a1 = random.randint(5,9)
	b1 = random.randint(0,4)
	a2 = random.randint(5,9)
	b2 = random.randint(0,4)
	print ' {'
	print '	"QuestionID" : %d,' % counter
	counter = counter + 1
	# want subtraction
	if (a1 * b1) > (a2 * b2):
		print '	"Question" : "What is %d + %d X %d - %d X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is %s plus %s times %s minus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = s1 + (a1 *b1) - (a2 * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		print '	"Most Common Mistake" : %d,' % ((s1 + a1) * b1 - (a2 * b2))
	# want addition
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
	print ' },'
	print ""
# for parentheses problems
for problem in range(50):
	s1 = random.randint(0,9)
	a1 = random.randint(5,9)
	b1 = random.randint(0,4)
	a2 = random.randint(5,9)
	b2 = random.randint(0,4)
	print ' {'
	print '	"QuestionID" : %d,' % counter
	counter = counter + 1
	# want subtraction
	if (a1 * b1) > (a2 * b2):
		print '	"Question" : "What is (%d + %d) X %d - %d X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is (%s plus %s) times %s minus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = (s1 + a1) *b1 - (a2 * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		print '	"Most Common Mistake" : %d,' % (s1 + (a1 * b1) - (a2 * b2))
	# want addition
	else:
		print '	"Question" : "What is %d + %d X (%d + %d) X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is %s plus %s times (%s plus %s) times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = s1 + (a1 *(b1 + a2) * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		print '	"Most Common Mistake" : %d,' % ((s1 + a1) * b1 + (a2 * b2))
	print '	"Difficulty Level" : 2,'
	print '	"Problem Type" : "Parentheses", '
	print '	"Max Time" : 60'
	print ' },'
	print ""


print "]"

