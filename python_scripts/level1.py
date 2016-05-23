from num2words import num2words
import random 


counter = 0
# for type multiplication
print "["
for number in range(20):
	for multiplier in range(11):
		number = random.randint(0,9)
		multiplier = random.randint(0,9)
		third  = random.randint(1,9)
		if counter % 2 == 0:
			print ' {'
			print '	"QuestionID" : %d,' % counter
			counter = counter + 1
			print '	"Question" : "What is %d + %d X %d?",' % (third, number, multiplier)
			print '	"Spoken Question" : "What is %s plus %s times %s?", ' % (num2words(third), num2words(number), num2words(multiplier))
			print '	"Answer" : %d,' % (third + (number * multiplier))
			print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(third + (number * multiplier)))
			print '	"Difficulty Level" : 1,'
			print '	"Problem Type" : "Multiplication", '
			print '	"Max Time" : 60,'
			print '	"Mistakes" : {'
			print '		"Common Mistake 1" : %d,' % ((third + number) * multiplier)
			print '		"Spoken Explanation 1" : "I think you added %s and %s before you multiplied by %s.",' % (num2words(third), num2words(number), num2words(multiplier))
			print '		"Written Explanation" : "Multiply %d X %d before adding %d to that product",' %(number, multiplier, third)
			print '		"Written General Feedback" : "%d X %d = %d; Now we have %d + %d = %d"' %(number, multiplier, number*multiplier, third, number*multiplier, third + (number*multiplier))
			print '	}'
			print ' },'
			print ""
		else:
			print ' {'
			print '	"QuestionID" : %d,' % counter
			counter = counter + 1
			# counter already an odd number
			if number % 2 == 0:
				print '	"Question" : "What is (%d + %d) X %d?",' % (third, number, multiplier)
				print '	"Spoken Question" : "What is (%s plus %s) times %s?", ' % (num2words(third), num2words(number), num2words(multiplier))
				answer = ((third + number) * multiplier)
				print '	"Answer" : %d,' % answer
			else:
				print '	"Question" : "What is %d X (%d + %d)?",' % (third, number, multiplier)
				print '	"Spoken Question" : "What is %s times (%s plus %s?)", ' % (num2words(third), num2words(number), num2words(multiplier))
				answer =  (third * (number + multiplier))
				print '	"Answer" : %d,' % answer
			print '	"Spoken Answer" : "The correct answer is %s.",' % (num2words(answer))
			print '	"Difficulty Level" : 1,'
			print '	"Problem Type" : "Parentheses", '
			print '	"Max Time" : 60,'
			if number % 2 == 0:
				print '	"Mistakes" : {'
				print '		"Common Mistake 1" : %d,' % (third + number * multiplier)
				print '		"Spoken Explanation 1" : "I think you multiplied %s and %s before you added %s.",' % (num2words(number), num2words(multiplier), num2words(third))
				print '		"Written Explanation" : "Add %d + %d before multiplying %d with that sum",' %(third,number, multiplier)
				print '		"Written General Feedback" : "%d + %d = %d; Now we have %d X %d = %d"' %(third, number,third + number, third + number, multiplier, ((third + number) * multiplier))
				print '	}'
			else:
				print '	"Mistakes" : {'
				print '		"Common Mistake 1" : %d,' % (third * number + multiplier)
				print '		"Spoken Explanation 1" : "I think you multiplied %s and %s before you added %s.",' % (num2words(third), num2words(number), num2words(multiplier))
				print '		"Written Explanation" : "Add %d + %d before multiplying %d with that sum",' %(multiplier,number, third)
				print '		"Written General Feedback" : "%d + %d = %d; Now we have %d X %d = %d"' %(number, multiplier, multiplier + number, third, multiplier + number,  third * (number + multiplier))
				print '	}'
			print ' },'
			print ""
print "]"