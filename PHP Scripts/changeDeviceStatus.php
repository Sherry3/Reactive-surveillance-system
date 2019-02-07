<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$query="Update Device set Status='".$_REQUEST['Status']."'  where DeviceId='".$_REQUEST['DeviceId']."';";

$result=mysql_query($query) or Die("".mysql_error());

mysql_close() or Die("".mysql_error());
?>