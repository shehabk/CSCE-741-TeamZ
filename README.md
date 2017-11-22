# CSCE-741-TeamZ

USING MYSQL DATABASE:(https://www.a2hosting.com/kb/developer-corner/mysql/managing-mysql-databases-and-users-from-the-command-line)
1) Install MySQL server. (remember the root password)
2) Do "mysql -u root -p" in the terminal, which will prompt for the password
   use the password you set while installing.
3) After this step do "GRANT ALL PRIVILEGES ON *.* TO teamz@'localhost' IDENTIFIED BY teamz;"
   it will create a new user named 'teamz' with password 'teamz'
4) Quit the mysql terminal
5) Login as new user with "mysql -u teamz -p", use teamz as password
6) Create a database named teamz "CREATE DATABASE teamz;"

YOU are now set up to use MySQL database for this project.
