<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT  ZoneId, Zone_Name FROM Zone where RegionId='".$_REQUEST['RegionId']."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{
    $output[]=$e;
}

print(json_encode($output));

mysql_close() or Die("".mysql_error());
?>