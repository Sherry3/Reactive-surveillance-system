<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q1=mysql_query("DELETE FROM Zone WHERE ZoneId='".$_REQUEST['ZoneId']."'") or Die("".mysql_error());

$q2=mysql_query("DELETE FROM ZoneMonitor where ZoneId='".$_REQUEST['ZoneId']."'") or Die("".mysql_error());

echo "Deleted";

mysql_close() or Die("".mysql_error());
?>