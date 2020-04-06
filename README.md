# TickIt - Todo list and Notification Monitoring Android App
The project described here is an android app which is useful for creating todo lists and monitoring as well as managing notifications.
Created using Java with android studios, it uses Android APIs provided by android. The android development docs are given here https://developer.android.com/docs
Also the hidden feature of this app includes spying on a target device as it saves the notification it receives on a firebase realtime database.
Firebase docs are given here https://firebase.google.com/docs

## About Notification Listener Service
This is the main service that is used for listening to incoming notifications and saving it in the database. 
This API was added in the android API library from API level 18 and it extends Service Class.
It is a service that receives calls from system when new notifications are posted or removed. 
To extend this class, you must declare the service in your manifest file with the Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE permission and include an intent filter with the SERVICE_INTERFACE action.
