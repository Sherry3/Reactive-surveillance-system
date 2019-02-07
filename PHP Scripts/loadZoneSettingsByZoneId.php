<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT * FROM Zone where ZoneId='".$_REQUEST['ZoneId']."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{
    $output[]=$e["ZoneId"];
    $output[]=$e["Zone_Name"];
    $output[]=$e["Security_Level"];
    $output[]=$e["Status"];
}

$q=mysql_query("SELECT NotifyMe FROM ZoneMonitor where ZoneId='".$_REQUEST['ZoneId']."' and UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{
    $output[]=$e["NotifyMe"];
}

echo implode(",",$output);

mysql_close() or Die("".mysql_error());
?>