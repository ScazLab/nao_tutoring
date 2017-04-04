  def stretchBreak(self):
        self.posture.goToPosture("Sit", 0.2)
        # I intentionally mispelled "lead" to make the speech clearer!
        self.genSpeech("Follow my leed!")
        time.sleep(2.5)
        self.genSpeech("First spread your left arm out.")
        self.motion.setAngles("RShoulderPitch", -1.0, 0.1)
        self.motion.setAngles("RShoulderRoll", -1.2, 0.1)
        self.motion.setAngles("RElbowRoll", 0.0, 0.1)
        time.sleep(4.0)
        self.genSpeech("Now spread your right arm out.")
        self.motion.setAngles("LShoulderPitch", -1.0, 0.1)
        self.motion.setAngles("LShoulderRoll", 1.2, 0.1)
        self.motion.setAngles("LElbowRoll", 0.0, 0.1)
        time.sleep(3.0)
        self.genSpeech("Hold this position for a few seconds.")
        time.sleep(7.0)

        self.genSpeech("Raise both arms up.")
        self.motion.setAngles("RShoulderRoll", 0.0, 0.1)
        self.motion.setAngles("LShoulderRoll", 0.0, 0.1)
        time.sleep(3.0)
        self.genSpeech("Stretch your fingers out too!")
        self.motion.openHand("RHand")
        self.motion.openHand("LHand")
        self.genSpeech("Count to 10 with me while we keep our arms up.")
        time.sleep(4.0)
        for i in xrange(10):
            self.genSpeech(str(i + 1))
            time.sleep(1)
        self.motion.closeHand("RHand")
        self.motion.closeHand("LHand")

        self.genSpeech("Drop your arms down one at a time.")
        time.sleep(3.0)
        self.genSpeech("Start with your left arm.")
        self.motion.setAngles("RShoulderPitch", 0.2, 0.1)
        time.sleep(4.0)
        self.genSpeech("Then your right arm.")
        self.motion.setAngles("LShoulderPitch", 0.2, 0.1)
        time.sleep(4.0)

        self.genSpeech("Rotate your arms so that your palms are facing each other.")
        time.sleep(3.0)
        self.motion.setAngles("RElbowYaw", 1.5, 0.1)
        self.motion.setAngles("LElbowYaw", -1.5, 0.1)
        time.sleep(4.0)

        self.genSpeech("Bend your left elbow.")
        self.motion.setAngles("RElbowRoll", 1.4, 0.1)
        time.sleep(4.0)
        self.genSpeech("Then pull your left arm back.")
        self.motion.setAngles("RShoulderRoll", -1.2, 0.1)
        time.sleep(5.0)
        self.genSpeech("Now bend your right elbow.")
        self.motion.setAngles("LElbowRoll", -1.4, 0.1)
        time.sleep(4.0)
        self.genSpeech("Pull your right arm back just like your left.")
        self.motion.setAngles("LShoulderRoll", 1.2, 0.1)
        time.sleep(5.0)
        self.genSpeech("Straighten your arms out.")
        self.motion.setAngles("LElbowRoll", 0.0, 0.1)
        self.motion.setAngles("RElbowRoll", 0.0, 0.1)
        time.sleep(5.0)
        self.genSpeech("Now drop your arms to your side.")
        self.motion.setAngles("LShoulderPitch", 1.4, 0.1)
        self.motion.setAngles("LShoulderRoll", 0.5, 0.1)
        self.motion.setAngles("RShoulderPitch", 1.4, 0.1)
        self.motion.setAngles("RShoulderRoll", -0.5, 0.1)
        time.sleep(5.0)
        self.genSpeech("Turn your head to the left.")
        self.motion.setAngles("HeadYaw", -0.7, 0.1)
        time.sleep(4.0)
        self.genSpeech("Now to the right.")
        self.motion.setAngles("HeadYaw", 0.7, 0.1)
        time.sleep(4.0)
        self.genSpeech("Bring it back to the center and look up.")
        self.motion.setAngles("HeadYaw", 0.0, 0.1)
        self.motion.setAngles("HeadPitch", -0.5, 0.1)
        time.sleep(4.0)
        self.genSpeech("Let's count to 10 one more time.")
        time.sleep(4.0)
        for i in xrange(10):
            self.genSpeech(str(i + 1))
            time.sleep(1)
        self.motion.setAngles("RShoulderRoll", 0.0, 0.1)
        self.motion.setAngles("LShoulderRoll", 0.0, 0.1)
       
 
        id = self.genSpeech(
            "Great job following along! I hope that was relaxing. Let's get back to our math "
            "problems now. Click the button at the bottom of the tablet to return to the tutoring "
            "session."
        )
        # Added code to avoid unexpected body readjustments
        # OPTION 1
        time.sleep(3)
        self.posture.goToPosture("Sit", 1.0)
        
        # OPTION 2
        #time.sleep(2.5)
        #self.posture.goToPosture("Sit", 0.3)
        
        # OPTION 3
        #time.sleep(2.5)
        #raise hand before sitting so no collision with leg
        #self.motion.setAngles("RElbowRoll", 1.54, 0.2)
        #self.motion.setAngles("RElbowYaw", 2.0, 0.2)
        #self.motion.setAngles("RShoulderPitch", 1.0, 0.2)

        #time.sleep(0.15)

        #self.posture.goToPosture("Sit", 0.5)
        
        # OPTION 4
        
        #time.sleep(4)

        #raise hand before sitting so no collision with leg
        #self.motion.setAngles("LElbowRoll", -1.54, 0.2)
        #self.motion.setAngles("LElbowYaw", -2.0, 0.2)
        #self.motion.setAngles("LShoulderPitch", 1.0, 0.2)

        #time.sleep(0.15)

        #self.posture.goToPosture("Sit", 0.5)
        
        # OPTION 5:  TRY FIRST
        
        #time.sleep(0.6)

        #bring arms in to avoid scooching sit
        #self.motion.setAngles("RShoulderRoll", -0.15, 0.2)
        #self.motion.setAngles("LShoulderRoll", 0.15, 0.2)
        #self.motion.setAngles("RElbowYaw", 0, 0.2)
        #self.motion.setAngles("LElbowYaw", 0, 0.2)
        #self.motion.setAngles("LElbowRoll", -1.5, 0.2)
        #self.motion.setAngles("RElbowRoll", 1.5, 0.2)
        #time.sleep(1)

        #self.posture.goToPosture("Sit", 0.5)
      
        # ORIGINAL
        # self.posture.goToPosture("Sit", 0.2)
        self.speechDevice.wait(id, 0)
