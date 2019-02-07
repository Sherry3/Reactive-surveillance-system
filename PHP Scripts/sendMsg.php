<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$SId=$_REQUEST["SUserId"];

if (strpos($_REQUEST["RUserId"],'usr') !== false)           //User
{   
    $q=mysql_query("SELECT Name FROM User where UserId='".$SId."'") or Die("".mysql_error());
    while($e=mysql_fetch_assoc($q))
    {
        $SName=$e["Name"];
    }

    $q=mysql_query("SELECT RegistrationId FROM User where UserId='".$_REQUEST["RUserId"]."'") or Die("".mysql_error());
    while($e=mysql_fetch_assoc($q))
    {
        $RId=$e["RegistrationId"];
    }    
}
else if (strpos($_REQUEST["RUserId"],'zn') !== false)         //Zone
{
    $q1=mysql_query("SELECT UserId FROM ZoneMonitor where ZoneId='".$_REQUEST["RUserId"]."' AND NotifyMe='true'") or Die("".mysql_error());
    while($e=mysql_fetch_array($q1))
    {
        $o[]=$e["UserId"];
    }
    
    $q=mysql_query("SELECT Zone_Name FROM Zone where ZoneId='".$_REQUEST["RUserId"]."'") or Die("".mysql_error());
    while($e=mysql_fetch_assoc($q))
    {
        $SName=$e["Zone_Name"];
        $SId=$_REQUEST["RUserId"];
    }

    $q=mysql_query("SELECT RegistrationId FROM User where UserId IN ('".implode("','",$o)."')") or Die("".mysql_error());
    while($e=mysql_fetch_assoc($q))
    {
        $RId=$e["RegistrationId"];
    }    
}
else 
{
    die("Invalid Reciepent");
}
                   
                   
push($RId, $SName, $SId);

mysql_close() or Die("".mysql_error());


function push($id, $SName, $UserId)
{
    
define("GOOGLE_API_KEY", "AIzaSyDkjOn7fhZadNdP_jglMsMYtDrZXKOd4tY"); 

$ids=array($id);

$message=array("msg" => $_REQUEST['Msg'], "name" => $SName, "link" => "flag", "UserId" => $UserId);
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
