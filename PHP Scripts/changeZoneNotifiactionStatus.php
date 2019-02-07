<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$query="Update ZoneMonitor set NotifyMe='".$_REQUEST['NotifyMe']."'  where ZoneId='".$_REQUEST['ZoneId']."' and UserId='".$_REQUEST['UserId']."'";

$result=mysql_query($query) or Die("".mysql_error());

mysql_close() or Die("".mysql_error());
?>