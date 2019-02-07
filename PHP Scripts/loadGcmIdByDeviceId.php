<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

//$q=mysql_query("SELECT ZoneId FROM Device where DeviceId='".$_REQUEST['DeviceId']."'") or Die("".mysql_error());
$q=mysql_query("SELECT ZoneId FROM Device where DeviceId='".$_REQUEST['DeviceId']."' and ZoneId not in (select ZoneId from Zone where status='off')") or Die("".mysql_error());
$e=mysql_fetch_assoc($q);

$zoneId=$e["ZoneId"];

$q1=mysql_query("SELECT RegionId FROM Zone where ZoneId='".$zoneId."'") or Die("".mysql_error());
while($e=mysql_fetch_array($q1))
{
    $regionId=$e["RegionId"];    
}

$q1=mysql_query("SELECT UserId FROM RegionMonitor where RegionId='".$regionId."' and NotifyMe='true'") or Die("".mysql_error());
while($e=mysql_fetch_array($q1))
{
    $o[]=$e["UserId"];    
}

$q1=mysql_query("SELECT UserId FROM ZoneMonitor where ZoneId='".$zoneId."' and NotifyMe='true'") or Die("".mysql_error());
while($e=mysql_fetch_array($q1))
{
    $o[]=$e["UserId"];    
}


$d="SELECT RegistrationId FROM User where UserId IN ('".implode("','",$o)."')";
$q2=mysql_query($d) or Die("".mysql_error());

while($e=mysql_fetch_assoc($q2))
{
    $output[]=$e["RegistrationId"];
}

//Call funtion to send notification
push($output);

//print(json_encode($output));

mysql_close() or Die("".mysql_error());


function push($ids)
{
    
define("GOOGLE_API_KEY", "AIzaSyDkjOn7fhZadNdP_jglMsMYtDrZXKOd4tY"); 

$message=array("msg" => "Intrusion", "link" => $_REQUEST['Link']);
$url = 'https://android.googleapis.com/gcm/send';

        $fields = array(
            'registration_ids' => $ids,
            'data' => $message,
        );

        $headers = array(
            'Authorization: key=' . 'AIzaSyDkjOn7fhZadNdP_jglMsMYtDrZXKOd4tY',
            'Content-Type: application/json'
        );
        //print_r($headers);
        // Open connection
        $ch = curl_init();
 
        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);
 
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
 
        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
 
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
 
        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
 
        // Close connection
        curl_close($ch);
        echo $result;
}

?>