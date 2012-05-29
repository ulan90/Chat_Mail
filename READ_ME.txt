1) Create the database "mail_db".
	(You have to create the "mail_db" because it's by default accessing to that database.
	Or just import "mail_db.sql" from "src" folder.
	After that when you run the "Server.java" file it will give error message. Press Ok,Change the
	"User Name: mail_login" and password to your own to enter the database.)

2) Attach the "mysql-connector-java-5.1.18.jar" to Eclipse(I don't know does it work on Net Beans) from "src\MYSQL_Connector".

3)Run "Server.java" file.

4) Run The "connectServerGui.java" file,then press connect button.

5) Registrate yourself or you can enter with "login:kantoro@mail.ru,password:123456"

6) Then you will see the list of users,the password of them is "123456" too.

P.S. I couldn't make the online users to green,offline users to red,but when the user logs in server is informs all
clients that a user entered a room.And if the client logs out server is informs all clients that a user left the room.
Or if some body registrates the list is refreshed automatically. I couldn't change the foreground of an online or offline
users,because I couldn't use "CellRenderer" class.