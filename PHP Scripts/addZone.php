<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$reg=$_REQUEST['RegionId'];
if($reg=="flag")
{
    $q1=mysql_query("SELECT RegionId FROM RegionMonitor where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());
    $e1=mysql_fetch_assoc($q1);
    $reg=$e1["RegionId"];
}

$q2=mysql_query("SELECT ZoneId FROM Zone") or Die("".mysql_error());

$max=0;
    
while($e2=mysql_fetch_assoc($q2))
{
    $temp=intval(str_replace("zn","",$e2["ZoneId"]));
    if($temp>$max)
        $max=$temp;
}

$max++;

$ZoneId="zn".$max;

$sl=intval($_REQUEST['SecurityLevel']);

$query="Insert into Zone values('$ZoneId','".$reg."','"
                                            .$_REQUEST['ZoneName']."', "
                                            .$sl.",'off',"
                                            ."NULL);";

$result=mysql_query($query)
or Die(mysql_error());

echo $ZoneId;

mysql_close() or Die("".mysql_error());
?>