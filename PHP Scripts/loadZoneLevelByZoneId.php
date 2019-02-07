<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT  Security_Level FROM Zone where ZoneId='".$_REQUEST['ZoneId']."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{
    $o[]=$e['Security_Level'];
}

echo $o[0];

mysql_close() or Die("".mysql_error());
?>