

PART II - Make a simple Android client
---------------------------------------

1. Open activities LoginActivity and ChatActivity

	* The first one is the main activity and the second one is the chat room. 
	
	* You're going to implement these activities. 

2. Open NetRequests and NetResponseHandler.

	* These classes are the base of aplication's networking. The first has methods for api requests. 
	
	* The second is a callback to handle asynchronous requests.
	
	* You must implement these classes according the API documentation [here][1]
	

3. Open classes in model package.

	* These classes model the API information model.
	
	* They're empty so you must implement them according the API documentation [here][1]
      

4. Add to LoginActivity a field to enter user nick and a button to login into the chat room.

	TIPS:
	
	* You may use the GUI designer provided by ADT 
	
	* Login button must store nick and navigate to ChatActivity on click.
	
	* Use SharedPreferences to store user information
	
5. Add to ChatActivity a scrollable area in which you'll write chat messages. 

6. Add to ChatActivity a field to enter messages and a button to send them.

7. Implement NetRequests.chatPOST and invoke it from Send Button's onclick

	TIPS: 
	
	* You may use HttpClient bundled with Android
	
	* Write message to screen on success
	
	* Remember to modify UI on a UI thread
	
8. Implement NetRequests.chatGET and invoke it from ChatActivity's onStart method

	TIPS:
	
	* Use [JSONObject][2] or other method to serialize/deserialize JSON
	
9. Invoke NetRequests.chatGET periodically.

	TIPS:
	
	* Use Android's [Handler.postDelayed][3]
	
	* Schedule next invocation on success
	
	* Handle Activity Lifecycle to stop scheduling
	 

Additional
-----------


Read about JUnit 3 testing [here][4] (Android Framework only supports JUnit 3) and about Android unit testing [here][5]

Unfortunately, current documentation refers to JUnit 4. You can find some doc. about JUnit 3 [here][6] and [here][7]



[1]: http://jira.mundoreader.com/confluence/display/Orpheus/CR+Kick-off+-+Training "KATA API"
[2]: http://developer.android.com/reference/org/json/JSONObject.html "JSONObject"
[3]: http://developer.android.com/reference/android/os/Handler.html
[4]: http://junit.sourceforge.net/junit3.8.1/ "JUnit 3"
[5]: http://developer.android.com/tools/testing/testing_android.html "Android Testing Fundamentals"
[6]: http://junit.sourceforge.net/doc/cookstour/cookstour.htm
[7]: http://pub.admc.com/howtos/junit3x/junit3x.html 