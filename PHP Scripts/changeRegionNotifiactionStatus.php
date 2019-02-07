<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$query="Update RegionMonitor set NotifyMe='".$_REQUEST['NotifyMe']."'  where RegionId='".$_REQUEST['RegionId']."' and UserId='".$_REQUEST['UserId']."'";

$result=mysql_query($query) or Die("".mysql_error());

mysql_close() or Die("".mysql_error());
?>