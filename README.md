# STool - Support Tool
STool is simple Java application created for the OO Programming Course in Laurea University of Applied Sciences.
It's a simple program that is designed to allow users to create new accounts and then create new support requests and view their own tickets. Admins can see tickets created by other users in addition to their own. Tickets can also be edited and deleted from the system.

# Installation
Two programs are required to run this application locally. First is Eclipse which is used as the development environment. Second program is XAMP which is used to run a database locally. Once these programs are installed you can download STool.zip. Within it you will find the SQL-script used to import tables and example data to an existing database as well as the project folder for Eclipse. Create a new database called <b>itstool</b> and import tables to it using the <b>itstool.sql</b> file. Last thing you need to do is to import the project folder to Eclipse as a new project. <b>Note!</b> You may get some errors while trying to execute the program. You may have to install <b>JDBC</b> in order to get the database functionality to work. Also make sure that the project folder is located under the XAMP directory (for example: C:\xampp\htdocs\STool).

# How-to-use
The program opens to a login window. Users can either login using existing credeantials or register by creating new ones. After logging in a window will open with currently active tickets created by the user. Normal users can only view their own tickets while admins can view everyones. The window also contains filters which can be used to choose whether you want to view avtice, closed or all tickets. The button for choosing whos tickets to display is greyed out for normal users.

The user can create new tickets by clicking the "New ticket" -button. This will open a small window which will be used to give data to the new ticket. Clicking "ok" will add the ticket to the database. By cliking an existing ticket the user can edit delete an old ticket. Clicking the "logout" -button will allow the user to switch between different users.
 
