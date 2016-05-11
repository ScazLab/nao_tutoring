from num2words import num2words
import random 

# initialize array of random order for numbers
# obviously, very unefficient, but for just 1 - 10, seems okay
first = [5, 8, 2, 9, 1, 3, 4, 0, 6, 8, 7]
second = [ 7, 5, 9, 2, 4, 6, 1, 8, 0, 3]
counter = 0
# for type multiplication
print "["
for number in first:
	for multiplier in second:
		third  = random.randint(0,9)
		print ' {'
		print '	"QuestionID" : %d,' % counter
		counter = counter + 1
		print '	"Question" : "What is %d + %d X %d?",' % (third, number, multiplier)
		print '	"Spoken Question" : "What is %s plus %s times %s?", ' % (num2words(third), num2words(number), num2words(multiplier))
		print '	"Answer" : %d,' % (third + (number * multiplier))
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(third + (number * multiplier)))
		print '	"Difficulty Level" : 1,'
		print '	"Problem Type" :"Multiplication", '
		print '	"Max Time" : 60,'
		print '	"Most Common Mistake" : %d' % ((third + number ) * multiplier)
		print ' },'
		print ""

# for type parentheses
for number in second:
	for multiplier in first:
		third  = random.randint(0,9)
		print ' {'
		print '	"QuestionID" : %d,' % counter
		counter = counter + 1
		print '	"Question" : "What is (%d + %d) X %d?",' % (third, number, multiplier)
		print '	"Spoken Question" : "What is (%s plus %s) times %s?", ' % (num2words(third), num2words(number), num2words(multiplier))
		print '	"Answer" : %d,' % ((third + number) * multiplier)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words((third + number) * multiplier))
		print '	"Difficulty Level" : 1,'
		print '	"Problem Type:" "Parentheses", '
		print '	"Max Time" : 60,'
		print '	"Most Common Mistake" : %d' % (third + number  * multiplier)
		print ' },'
		print ""
print "]"