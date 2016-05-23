from num2words import num2words
import random 


counter = 320
print "["
for problem in range(100):
	print ' {'
	print '	"QuestionID" : %d,' % counter
	counter = counter + 1	
	if counter%2 ==0:
		s1 = random.randint(10,99)
		a1 = random.randint(5,9)
		b1 = random.randint(5,9)
		a2 = random.randint(5,9)
		b2 = random.randint(0,4)
		
		# want subtraction
		if (a1 * b1) > (a2 * b2):
			print '	"Question" : "What is %d + %d X %d - %d X %d?",' % (s1, a1, b1, a2, b2)
			print '	"Spoken Question" : "What is %s plus %s times %s minus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
			answer = s1 + (a1 *b1) - (a2 * b2)
			print '	"Answer" : %d,' % (answer)
			print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
			commonmistake1 =  ((s1 + a1) * b1 - (a2 * b2))
			spokenexplanation1 = "Remember to do multiplication first! So multiply %s by %s and %s by %s" % (num2words(a1), num2words(b1), num2words(a2), num2words(b2))
			print '	"Mistakes" : {'
			print '		"Common Mistake 1" : %d,'% commonmistake1
			print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
			print '		"Written Explanation" : "Multiply %s by %s and %s by %s, and subtract the second product from the first. Then, add %s to that difference",' % ((a1), (b1), (a2), (b2), (s1))
			print ' 	"Written General Feedback" : "%d X %d = %d; %d X %d = %d; Now we have %d + %d - %d; %d + %d = %d; Now we have %d - %d = %d"' %(a1,b1, a1*b1, a2, b2, a2*b2, s1, a1*b1, a2*b2, s1, a1*b1, s1 + a1*b1,s1 + a1*b1, a2*b2, answer)
			print '	},'
		# want addition
		else:
			print '	"Question" : "What is %d + %d X %d + %d X %d?",' % (s1, a1, b1, a2, b2)
			print '	"Spoken Question" : "What is %s plus %s times %s plus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
			answer = s1 + (a1 *b1) + (a2 * b2)
			print '	"Answer" : %d,' % (answer)
			print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
			commonmistake1 =  ((s1 + a1) * b1 + (a2 * b2))
			spokenexplanation1 = "Remember to do multiplication first! So multiply %s by %s and %s by %s" % (num2words(a1), num2words(b1), num2words(a2), num2words(b2))
			print '	"Mistakes" : {'
			print '		"Common Mistake 1" : %d,'% commonmistake1
			print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
			print '		"Written Explanation" : "Multiply %s by %s and %s by %s, and add the second product to the first. Then, add %s to that sum",' % ((a1), (b1), (a2), (b2), (s1))
			print ' 	"Written General Feedback" : "%d X %d = %d; %d X %d = %d; Now we have %d + %d + %d; %d + %d = %d; Now we have %d + %d = %d"' %(a1,b1, a1*b1, a2, b2, a2*b2, s1, a1*b1, a2*b2, s1, a1*b1, s1 + a1*b1,s1 + a1*b1, a2*b2, answer)
			print '	},'

		print '	"Difficulty Level" : 3,'
		print '	"Problem Type" : "Multiplication", '
		print '	"Max Time" : 60'
		print ' },'
		print ""
	else:
		s1 = random.randint(0,9)
		a1 = random.randint(5,9)
		b1 = random.randint(0,4)
		a2 = random.randint(5,9)
		b2 = random.randint(0,4)
		# want subtraction
		if (a1 * b1) > (a2 * b2):
			print '	"Question" : "What is (%d + %d) X %d - %d X %d?",' % (s1, a1, b1, a2, b2)
			print '	"Spoken Question" : "What is (%s plus %s) times %s minus %s times %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
			answer = (s1 + a1) *b1 - (a2 * b2)
			print '	"Answer" : %d,' % (answer)
			print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
			commonmistake1 =  (((s1 + a1) * b1) - a2)*b2
			spokenexplanation1 = "Remember to do multiplication first! So multiply %s by %s before you do any subtraction" % (num2words(a2), num2words(b2))
			print '	"Mistakes" : {'
			print '		"Common Mistake 1" : %d,'% commonmistake1
			print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
			print '		"Written Explanation" : "Add %d and %d, and multiply %d by that sum. Then subtract the product of %d and %d from that product",' % (s1, (a1), (b1), (a2), (b2))
			print ' 	"Written General Feedback" : "%d + %d = %d; Now we have: %d X %d - %d X %d; %d X %d = %d; %d X %d = %d; Now we have: %d - %d = %d"' % (s1, a1, s1 + a1, s1+a1, b1, a2, b2, s1+a1, b1, (s1+a1)*b1, a2, b2,a2*b2,(s1+a1)*b1, a2*b2, answer)
			print '	},'
		# want addition
		else:
			print '	"Question" : "What is %d + %d X (%d + %d) + %d?",' % (s1, a1, b1, a2, b2)
			print '	"Spoken Question" : "What is %s plus %s times (%s plus %s) plus %s?", ' % (num2words(s1),num2words(a1), num2words(b1), num2words(a2), num2words(b2))
			answer = s1 + (a1 *(b1 + a2) + b2)
			print '	"Answer" : %d,' % (answer)
			print '	"Spoken Answer" : "The correct answer is %s.",' %(num2words(answer))
			commonmistake1 = (s1+a1)*(b1+a2) + b2 
			spokenexplanation1 = "Remember to do multiplication first! So multiply %s by the sum of %s and %s before you do any subtraction" % (num2words(a1), num2words(b1), num2words(a2))
			print '	"Mistakes" : {'
			print '		"Common Mistake 1" : %d,'% commonmistake1
			print '		"Spoken Explanation 1" : " %s",' % (spokenexplanation1)
			print '		"Written Explanation" : "Add %d and %d, and multiply %d by that sum. Then add %s to that product, and then add %s to that sum",' % (b1, (a2), (a1), (s1), (b2))
			print ' 	"Written General Feedback" : "%d + %d = %d; Now we have: %d + %d X %d + %d;  %d X %d = %d; Now we have: %d + %d + %d; %d + %d = %d; Now we have %d + %d = %d"' % (b1, a2, b1 + a2, s1, a1, b1 + a2, b2, a1, b1 + a2, (b1 + a2)*a1, s1, (b1 + a2)*a1, b2, s1, ((b1 + a2)*a1), ((b1 + a2)*a1) + s1, ((b1 + a2)*a1) + s1, b2, answer)
			print '	},'
		print '	"Difficulty Level" : 2,'
		print '	"Problem Type" : "Parentheses", '
		print '	"Max Time" : 60'
		print ' },'
		print ""
print "]"

