<?php

include('addUser.php');

include('credentials.php');
$c=new me;

$UserId=addUser($c) or die("functional error");
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$query="Insert into ZoneMonitor values('".$_REQUEST['ZoneId']."','".$UserId."','true');";

$result=mysql_query($query)
or Die(mysql_error());

mysql_close() or Die("".mysql_error());

?>