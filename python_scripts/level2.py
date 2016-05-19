from num2words import num2words
import random 

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
		commonmistake1 =  ((s1 + a1) * b1 - (a2 * b2))
		spokenexplanation1 = "Remember to do multiplication first! So multiply %s by %s and %s by %s" % (num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		print '		"Mistakes" : {'
		print '		"Common Mistake 1" : %d,'% commonmistake1
		print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
		print '		"Written Explanation" : "Multiply %s by %s and %s by %s, and subtract the second product from the first. Then, add %s to that difference",' % (num2words(a1), num2words(b1), num2words(a2), num2words(b2), num2words(s1))
		print ' 	"Written General Feedback" : "%d X %d = %d; %d X %d = %d; %d - %d = %d; %d + %d = %d"' %(a1,b1, a1*b1, a2, b2, a2*b2, a1*b1, a2*b2,a1*b1- a2*b2, s1, a1*b1- a2*b2, answer)
		print '		},'

	# want addition
	else:
		print '	"Question" : "What is %d + %d X %d + %d X %d?",' % (s1, a1, b1, a2, b2)
		print '	"Spoken Question" : "What is %s plus %s times %s plus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		answer = s1 + (a1 *b1) + (a2 * b2)
		print '	"Answer" : %d,' % (answer)
		print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
		commonmistake1 =  ((s1 + a1) * b1 + (a2 * b2))
		spokenexplanation1 = "Remember to do multiplication first! So multiply %s by %s and %s by %s" % (num2words(a1), num2words(b1), num2words(a2), num2words(b2))
		print '		"Mistakes" : {'
		print '		"Common Mistake 1" : %d,'% commonmistake1
		print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
		print '		"Written Explanation" : "Multiply %s by %s and %s by %s, and add the second product to the first. Then, add %s to that sum",' % (num2words(a1), num2words(b1), num2words(a2), num2words(b2), num2words(s1))
		print ' 	"Written General Feedback" : "%d X %d = %d; %d X %d = %d; %d + %d = %d; %d + %d = %d"' %(a1,b1, a1*b1, a2, b2, a2*b2, a1*b1, a2*b2,a1*b1 + a2*b2, s1, a1*b1 + a2*b2, answer)
		print '		},'
	print '	"Difficulty Level" : 2,'
	print '	"Problem Type" : "Multiplication", '
	print '	"Max Time" : 60'
	print ' },'
	print ""
# for parentheses problems
#old level2 parenthese became level3 parentheses
for problem in range(50):
	times = random.randint(0,9)
	outsideAdd = random.randint(0,9)
	firstadd = random.randint(0,4)
	secondadd = random.randint(5,9)
	#make sure multiplied not greater than 9
	while(firstadd + secondadd > 9):
		firstadd = random.randint(0,4)
		secondadd= random.randint(5,9)
	print ' {'
	print '	"QuestionID" : %d,' % counter
	counter = counter + 1
	# parentheses in front
	if counter%2 == 0:
		print '	"Question" : "What is (%d + %d) X %d + %d?",' % (firstadd, secondadd, times, outsideAdd)
		print '	"Spoken Question" : "What is (%s plus %s) times %s plus %s?",' % (num2words(firstadd), num2words(secondadd), num2words(times), num2words(outsideAdd))
		commonmistake1 = (firstadd + secondadd) *(times + outsideAdd)
		spokenexplanation1 = "Remember that once you've done what's inside the parentheses, multiply by %s next." % (num2words(times))
		commonmistake2 = (secondadd * times) + outsideAdd + firstadd
		spokenexplanation2 = "Remember to do what's inside the parentheses first, by adding %s and %s." %(num2words(firstadd), num2words(secondadd))
	# parentheses in middle
	else:
		print '	"Question" : "What is %d + (%d + %d) X %d)?",' % (outsideAdd, firstadd, secondadd, times)
		print '	"Spoken Question" : "What is %s plus (%s plus %s) times %s?", ' % (num2words(outsideAdd),num2words(firstadd), num2words(secondadd), num2words(times))
		commonmistake1	= (firstadd + secondadd + outsideAdd) * times
		spokenexplanation1 = "Remember that once you've done what's inside the parentheses, multiply by %s next." %num2words(times)
		commonmistake2 = (secondadd * times) + outsideAdd + firstadd
		spokenexplanation2 = "Remember to do what's inside the parentheses first, by adding %s and %s." %(num2words(firstadd), num2words(secondadd))
	answer = ((firstadd + secondadd) * times) + outsideAdd
	print '	"Answer" : %d,' % (answer)
	print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
	print '		"Mistakes" : {'
	print '		"Common Mistake 1" : %d,'% commonmistake1
	print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
	print '		"Common Mistake 2" : %d,' % commonmistake2
	print '		"Spoken Explanation 2" : " %s",' % (spokenexplanation2)

	print '		"Written Explanation" : "Add %s to %s before multiplying %s with that sum, and then add %s to that product.",' % (num2words(firstadd), num2words(secondadd), num2words(times), num2words(outsideAdd))
	print ' 	"Written General Feedback" : "%d + %d = %d; %d X %d = %d; %d + %d = %d"' %(firstadd, secondadd, firstadd+secondadd, firstadd+secondadd, times, (firstadd+secondadd)*times,(firstadd+secondadd)*times, outsideAdd, answer)
	print '		},'


	print '	"Difficulty Level" : 2,'
	print '	"Problem Type" : "Parentheses", '
	print '	"Max Time" : 60'
	print ' },'
	print ""


print "]"

