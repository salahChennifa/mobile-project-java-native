<?php
require_once 'DbConnect.php';


$response = array();

if (isset($_GET['apicall'])) {
    
    switch ($_GET['apicall']) {
        
        case 'login':
            
            if (isTheseParametersAvailable(array(
                'userEmail',
                'userPasswd'
            ))) {
                
                $userEmail  = $_POST['userEmail'];
                $userPasswd = ($_POST['userPasswd']);
                
                
                
                $stmt = $conn->prepare("SELECT userID, userEmail, userPasswd, userPhone,userFName,userLName,userType  FROM User WHERE userEmail = ? AND userPasswd = ?");
                $stmt->bind_param("ss", $userEmail, $userPasswd);
                
                $stmt->execute();
                
                $stmt->store_result();
                
                if ($stmt->num_rows > 0) {
                    
                    $stmt->bind_result($id, $userEmail, $userPasswd, $userPhone, $userFName, $userLName, $userType);
                    $stmt->fetch();
                    
                    $user = array(
                        'userID' => $id,
                        'userPasswd' => $userPasswd,
                        'userEmail' => $userEmail,
                        'userPhone' => $userPhone,
                        'userFName' => $userFName,
                        'userLName' => $userLName,
                        'userType' => $userType
                    );
                    
                    $response['error']   = false;
                    $response['message'] = 'Login successfull';
                    $response['user']    = $user;
                } else if ($stmt->num_rows === 0) {
                    $stmt1 = $conn->prepare("SELECT clientID,fName, clientEmail, passwd, clientPhone,identityNumber,lName  FROM Clients WHERE clientEmail = ? AND passwd = ?");
                    $stmt1->bind_param("ss", $userEmail, $userPasswd);
                    $stmt1->execute();
                    $stmt1->store_result();
                    if ($stmt1->num_rows > 0) {
                        $stmt1->bind_result($clientID, $fName, $clientEmail, $passwd, $clientPhone, $identityNumber, $lName);
                        $stmt1->fetch();
                        $client              = array(
                            'clientID' => $clientID,
                            'fName' => $fName,
                            'clientEmail' => $clientEmail,
                            'passwd' => $passwd,
                            'clientPhone' => $clientPhone,
                            'identityNumber' => $identityNumber,
                            'lName' => $lName,
                            'userType' => "Client"
                        );
                        $response['error']   = false;
                        $response['message'] = 'Login successfull';
                        $response['user']    = $client;
                    }
                    
                    
                } else {
                    
                    $response['error']   = false;
                    $response['message'] = 'Invalid username or password';
                }
            }
            break;
        case 'modtSl':
            if (isTheseParametersAvailable(array(
                'userEmail',
                'userPasswd',
                'userFName',
                'userLName',
                'slEmail',
                'passwd',
                'userPhone'
            ))) {
                 
                $userEmail  = $_POST['userEmail'];
                $userPasswd = ($_POST['userPasswd']);
                $userFName  = $_POST['userFName'];
                $userLName  = $_POST['userLName'];
                $slEmail    = $_POST['slEmail'];
                $passwd     = $_POST['passwd'];
                $userPhone  = $_POST['userPhone'];
                
               
                $stmt1 = $conn->prepare("SELECT *  FROM User WHERE userEmail = ? AND userPasswd = ?");
                $stmt1->bind_param("ss", $userEmail, $userPasswd);
                $stmt1->execute();
                $stmt1->store_result();
                
                if ($stmt1->num_rows > 0) {
                    
                    $sql = "UPDATE User SET userFName = '$userFName',  userLName = '$userLName', userEmail = '$slEmail', userPasswd = '$passwd', userPhone = '$userPhone'  WHERE (userEmail = '$userEmail' AND userPasswd = '$userPasswd')";
                    
                    
                    if ($conn->query($sql) === TRUE) {
                        
                        $user = array(
                            'userFName' => $userFName,
                            'userLName' => $userLName,
                            'slEmail' => $slEmail,
                            'passwd' => $passwd,
                            'userPhone' => $userPhone,
                            'userEmail' => $userEmail,
                            'userPasswd' => $userPasswd
                        );
                        
                        $response['error']   = false;
                        $response['message'] = 'UpdateSecces successfull';
                        $response['user']    = $user;
                    }
                    
                } else {
                    $response['error']   = false;
                    $response['message'] = "the row not update";
                }
            }
            mysqli_close($conn);
            
            break;
        case 'selectClt':
            
            
            $sql = "SELECT *  FROM Clients";
            
            $clients = array();
            $result  = $conn->query($sql);
            if ($result->num_rows > 0) {
                while ($row = mysqli_fetch_array($result)) {
                    array_push($clients, array(
                        "clientID" => $row[0],
                        "clientEmail" => $row[11],
                        "passwd" => $row[12],
                        "clientPhone" => $row[13],
                        "fName" => $row[2],
                        "lName" => $row[3]
                    ));
                }
            }
            $response['clients'] = $clients;
            
            break;
        case 'modtClt':
            if (isTheseParametersAvailable(array(
                'userEmail',
                'userPasswd',
                'fName',
                'lName',
                'clientEmail',
                'passwd',
                'clientPhone'
            ))) {
                $userEmail   = $_POST['userEmail'];
                $userPasswd  = ($_POST['userPasswd']);
                $fName       = $_POST['fName'];
                $lName       = $_POST['lName'];
                $clientEmail = $_POST['clientEmail'];
                $passwd      = $_POST['passwd'];
                $clientPhone = $_POST['clientPhone'];
                $stmt1 = $conn->prepare("SELECT *  FROM Clients WHERE clientEmail = ? AND passwd = ?");
                $stmt1->bind_param("ss", $userEmail, $userPasswd);
                $stmt1->execute();
                $stmt1->store_result();
                
                if ($stmt1->num_rows > 0) {
                    $sql = "UPDATE Clients SET fName = '$fName', lName = '$lName', clientEmail = '$clientEmail', passwd = '$passwd', clientPhone = '$clientPhone'  WHERE (clientEmail = '$userEmail' AND passwd = '$userPasswd')";
                    
                    
                    if ($conn->query($sql) === TRUE) {
                        
                        $user = array(
                            'fName' => $fName,
                            'lName' => $lName,
                            'clientEmail' => $clientEmail,
                            'passwd' => $passwd,
                            'clientPhone' => $clientPhone,
                            'userEmail' => $userEmail,
                            'userPasswd' => $userPasswd
                        );
                        
                        $response['error']   = false;
                        $response['message'] = 'UpdateSecces successfull';
                        $response['user']    = $user;
                    }
                    
                } else {
                    $response['error']   = false;
                    $response['message'] = "the row not update";
                }
            }
            mysqli_close($conn);
            
            break;
        
        case 'ajouterClt':
            if (isTheseParametersAvailable(array(
                'userPasswd',
                'fName',
                'lName',
                'clientEmail',
                'clientPhone'
            ))) {
                $userPasswd  = ($_POST['userPasswd']);
                $fName       = $_POST['fName'];
                $lName       = $_POST['lName'];
                $clientEmail = $_POST['clientEmail'];
                $clientPhone = $_POST['clientPhone'];
                // --------------------------------
                $stmt1       = $conn->prepare("SELECT *  FROM Clients WHERE clientEmail = ? AND passwd = ?");
                $stmt1->bind_param("ss", $userEmail, $userPasswd);
                $stmt1->execute();
                $stmt1->store_result();
                //----------------------------------------------------
                
                if ($stmt1->num_rows > 0) {
                    echo "error";
                    $response['error']   = true;
                    $response['message'] = "This clients already exists";
                    
                } else {
                    $sql = "INSERT INTO Clients  (clientEmail, passwd, fName,lName,clientPhone)  VALUES ('$clientEmail', '$userPasswd', '$fName','$lName','$clientPhone')";
                    
                    
                    if ($conn->query($sql) === TRUE) {
                        $user = array(
                            'fName' => $fName,
                            'lName' => $lName,
                            'clientEmail' => $clientEmail,
                            'passwd' => $userPasswd,
                            'clientPhone' => $clientPhone
                        );
                        
                        $response['error']   = false;
                        $response['message'] = 'Added Cllinet successfull';
                        $response['Client']  = $user;
                    }
                }
            }
            mysqli_close($conn);
            
            break;
        
        case 'addtseance':
            if (isTheseParametersAvailable(array(
                'clientID',
                'monitorID',
                'startDate',
                'durationMinut'
            ))) {
                $clientID      = $_POST["clientID"];
                $monitorID     = $_POST['monitorID'];
                $startDate     = $_POST['startDate'];
                $durationMinut = $_POST['durationMinut'];
                $sql           = "INSERT INTO Seances  (clientID,monitorID, startDate, durationMinut)  VALUES ('$clientID','$monitorID', ' $startDate', '$durationMinut')";
                if ($conn->query($sql) === TRUE) {
                    $seance = array(
                        'clientID' => $clientID,
                        'monitorID' => $monitorID,
                        'startDate' => $startDate,
                        'durationMinut' => $durationMinut
                    );
                    
                    $response['error']   = false;
                    $response['message'] = 'Added Seance successfull';
                    $response['seance']  = $seance;
                } else {
                    $response['error']   = false;
                    $response['message'] = 'Added Seance NOT successfull';
                }
            }
            mysqli_close($conn);
            
            break;
        
        case 'addtTask':
            if (isTheseParametersAvailable(array(
                'user_Fk',
                'title',
                'startDate',
                'durationMinut'
            ))) {
                
                $user_Fk       = $_POST['user_Fk'];
                $title         = $_POST['title'];
                $startDate     = $_POST['startDate'];
                $durationMinut = $_POST['durationMinut'];
                $sql           = "INSERT INTO Tasks  (user_Fk,title, startDate, durationMinut)  VALUES ('$user_Fk','$title', ' $startDate', '$durationMinut')";
                if ($conn->query($sql) === TRUE) {
                    $seance = array(
                        'user_Fk' => $user_Fk,
                        'title' => $title,
                        'startDate' => $startDate,
                        'durationMinut' => $durationMinut
                    );
                    
                    $response['error']   = false;
                    $response['message'] = 'Added Task successfull';
                    $response['seance']  = $seance;
                } else {
                    $response['error']   = false;
                    $response['message'] = 'Added task NOT successfull';
                }
            }
            mysqli_close($conn);
            
            break;
        
        case 'listClsSlr':
            if (isTheseParametersAvailable(array(
                'id_current_monitor'
            ))) {
                
                
                $id_current_monitor = $_POST["id_current_monitor"];
               
                $sql1   = "SELECT DISTINCT clientID  FROM Seances WHERE monitorID = $id_current_monitor";
                
                $work   = array();
                $result = $conn->query($sql1);
                $cls    = array();
                if ($result->num_rows > 0) {
                    
                    while ($row = mysqli_fetch_array($result)) {
                        
                        $cls[] = $row[0];
                    }
                }
                
                $clients = array();
                
                for ($i = 0; $i < count($cls); $i++) {
                    
                    $sql     = "SELECT *  FROM Clients WHERE clientID= '$cls[$i]' ";
                    $result1 = $conn->query($sql);
                    if ($result1->num_rows > 0) {
                        while ($row = mysqli_fetch_array($result1)) {
                            array_push($clients, array(
                                "clientID" => $row[0],
                                "clientEmail" => $row[11],
                                "passwd" => $row[12],
                                "clientPhone" => $row[13],
                                "fName" => $row[2],
                                "lName" => $row[3]
                            ));
                        }
                    }
                }
                
                $response['clients'] = $clients;
            }
            mysqli_close($conn);
            
            break;
        case 'listClsSlrTime':
            if (isTheseParametersAvailable(array(
                'id_current_monitor',
                'startDate'
            ))) {
                
                
                $id_current_monitor = $_POST["id_current_monitor"];
                $startDate          = $_POST["startDate"];
                $sql1               = "SELECT  clientID, isDone  FROM Seances WHERE monitorID = $id_current_monitor and startDate = '$startDate'";
                
                $result = $conn->query($sql1);
                $cls    = array();
                if ($result->num_rows > 0) {
                    
                    while ($row = mysqli_fetch_array($result)) {
                        $cls[] = $row[0];
                    }
                }
                
                $clients = array();
                for ($i = 0; $i < count($cls); $i++) {
                    echo $cls[$i];
                }
                
                for ($i = 0; $i < count($cls); $i++) {
                    
                    $sql     = "SELECT *  FROM Clients WHERE clientID= '$cls[$i]' ";
                    $result1 = $conn->query($sql);
                    if ($result1->num_rows > 0) {
                        while ($row = mysqli_fetch_array($result1)) {
                            array_push($clients, array(
                                "clientID" => $row[0],
                                "clientEmail" => $row[11],
                                "passwd" => $row[12],
                                "clientPhone" => $row[13],
                                "fName" => $row[2],
                                "lName" => $row[3]
                            ));
                        }
                    }
                }
                
                $response['clients'] = $clients;
            }
            mysqli_close($conn);
            
            break;
        
        case 'listGrpCl':
            if (isTheseParametersAvailable(array(
                'id_current_monitor',
                'startDate'
            ))) {
                $id_current_monitor = $_POST["id_current_monitor"];
                $startDate          = $_POST["startDate"];
                $sql1               = "SELECT   seanceID, seanceGrpID, isDone, clientID, startDate,durationMinut  FROM Seances WHERE monitorID = $id_current_monitor and  startDate LIKE '$startDate%'";
                $result             = $conn->query($sql1);
                $isDone             = array();
                $seanceIDs          = array();
                $group;
                $clientIDs      = array();
                $startDates     = array();
                $durationMinuts = array();
                if ($result->num_rows > 0) {
                    
                    while ($row = mysqli_fetch_array($result)) {
                        $isDone[]         = $row[2];
                        $group            = $row[1];
                        $clientIDs[]      = $row[3];
                        $startDates[]     = $row[4];
                        $durationMinuts[] = $row[5];
                        $seanceIDs[]      = $row[0];
                    }
                }
                
                $clients = array();
                for ($i = 0; $i < count($clientIDs); $i++) {
                    
                    $sql     = "SELECT *  FROM Clients WHERE clientID= '$clientIDs[$i]' ";
                    $result1 = $conn->query($sql);
                    if ($result1->num_rows > 0) {
                        
                        while ($row = mysqli_fetch_array($result1)) {
                            array_push($clients, array(
                                "clientID" => $row[0],
                                "clientEmail" => $row[11],
                                "passwd" => $row[12],
                                "clientPhone" => $row[13],
                                "fName" => $row[2],
                                "lName" => $row[3],
                                "isDone" => $isDone[$i],
                                "startDate" => $startDates[$i],
                                "durationMinut" => $durationMinuts[$i],
                                "seanceID" => $seanceIDs[$i]
                            ));
                            
                        }
                    }
                }
                
                $response['clients'] = $clients;
            }
            mysqli_close($conn);
            
            break;
        case 'listCltSeance':
            if (isTheseParametersAvailable(array(
                'id_current_client',
                'start_date_url'
            ))) {
                $start_date_url    = $_POST["start_date_url"];
                $id_current_client = $_POST["id_current_client"];
                $sql1              = "SELECT   monitorID, isDone, seanceGrpID, startDate,durationMinut, seanceID  FROM Seances WHERE clientID = $id_current_client and  startDate LIKE '$start_date_url%'";
                
                $result         = $conn->query($sql1);
                $Goups          = array();
                $seanceIDs      = array();
                $isDone         = array();
                $monitorIDs     = array();
                $startDates     = array();
                $durationMinuts = array();
                if ($result->num_rows > 0) {
                    $index = 0;
                    while ($row = mysqli_fetch_array($result)) {
                        $monitorIDs[]     = $row[0];
                        $isDone[]         = $row[1];
                        $Groups[]         = $row[2];
                        $startDates[]     = $row[3];
                        $durationMinuts[] = $row["durationMinut"];
                        $seanceIDs[]      = $row["seanceID"];
                        
                    }
                }
                $output   = array_values(array_unique($monitorIDs));
                $monitors = array();
                
                for ($i = 0; $i < count($monitorIDs); $i++) {
                    
                    $sql     = "SELECT *  FROM User WHERE userID = '$monitorIDs[$i]' ";
                    $result1 = $conn->query($sql);
                    if ($result1->num_rows > 0) {
                        // output data of each row
                        
                        while ($row = mysqli_fetch_array($result1)) {
                            array_push($monitors, array(
                                "userFName" => $row[7],
                                "userLName" => $row[8],
                                "userPhone" => $row[13],
                                "userEmail" => $row[2],
                                "isDone" => $isDone[$i],
                                "startDate" => $startDates[$i],
                                "durationMinut" => $durationMinuts[$i],
                                "monitorID" => $monitorIDs[$i],
                                "IDSeance" => $seanceIDs[$i]
                            ));
                            
                        }
                    }
                }
                
                $response['monitors'] = $monitors;
            }
            mysqli_close($conn);
            
            break;
        
        case 'listOfMonitors':
            $monitors = array();
            $sql      = "SELECT userID, userFName, userLName, userPhone  FROM User WHERE userType ='MONITOR' ";
            $result1  = $conn->query($sql);
            if ($result1->num_rows > 0) {
                while ($row = mysqli_fetch_array($result1)) {
                    array_push($monitors, array(
                        "userID" => $row[0],
                        "userFName" => $row[1],
                        "userLName" => $row[2],
                        "userPhone" => $row[3]
                    ));
                    
                }
                $response['error'] = false;
            } else {
                $response['error'] = true;
            }
            
            
            $response['monitors'] = $monitors;
            
            mysqli_close($conn);
            
            break;
        case 'modtseance':
            if (isTheseParametersAvailable(array(
                'seanceID',
                'monitorID',
                'startDate',
                'durationMinut'
            ))) {
                $seanceID      = $_POST['seanceID'];
                $monitorID     = $_POST['monitorID'];
                $startDate     = $_POST['startDate'];
                $durationMinut = $_POST['durationMinut'];
                $stmt1         = $conn->prepare("SELECT *  FROM Seances WHERE seanceID = ?");
                $stmt1->bind_param("s", $seanceID);
                $stmt1->execute();
                $stmt1->store_result();
                
                if ($stmt1->num_rows > 0) {
                    $sql = "UPDATE Seances SET monitorID = '$monitorID', startDate = '$startDate', durationMinut = '$durationMinut' WHERE (seanceID = '$seanceID')";
                    if ($conn->query($sql) === TRUE) {
                        $seance = array(
                            'seanceID' => $seanceID,
                            'monitorID' => $monitorID,
                            'startDate' => $startDate,
                            'durationMinut' => $durationMinut
                        );
                        
                        $response['error']   = false;
                        $response['message'] = 'UpdateSecces seances successfull';
                        $response['seance']  = $seance;
                    }
                    
                } else {
                    $response['error']   = false;
                    $response['message'] = "the row not update";
                }
            }
            mysqli_close($conn);
            
            break;
        
        
        case 'modtTask':
            if (isTheseParametersAvailable(array(
                'taskID',
                'user_Fk',
                'startDate',
                'durationMinut',
                'title'
            ))) {
                
                
                $taskID        = $_POST['taskID'];
                $title         = ($_POST['title']);
                $user_Fk       = $_POST['user_Fk'];
                $startDate     = $_POST['startDate'];
                $durationMinut = $_POST['durationMinut'];
                
                $stmt1 = $conn->prepare("SELECT *  FROM Tasks WHERE taskID = ?");
                $stmt1->bind_param('s', $taskID);
                $stmt1->execute();
                $stmt1->store_result();
                
                if ($stmt1->num_rows > 0) {
                    
                    $sql = "UPDATE Tasks SET   startDate = '$startDate', durationMinut = '$durationMinut', title = '$title' WHERE (taskID = '$taskID')";
                    
                    if ($conn->query($sql) === TRUE) {
                        
                        $seance = array(
                            'taskID' => $taskID,
                            'user_Fk' => $user_Fk,
                            'startDate' => $startDate,
                            'durationMinut' => $durationMinut,
                            'title' => $title
                        );
                        
                        $response['error']   = false;
                        $response['message'] = 'UpdateSecces Task successfull';
                        $response['seance']  = $seance;
                    }
                    
                } else {
                    $response['error']   = false;
                    $response['message'] = "the row not update";
                }
            }
            mysqli_close($conn);
            
            break;
        
        
        case 'modtSeanceMonitor':
            if (isTheseParametersAvailable(array(
                'id_seance',
                'value_of_prensence'
            ))) {
                $seanceID           = $_POST['id_seance'];
                $value_of_prensence = $_POST['value_of_prensence'];
                
                $stmt1 = $conn->prepare("SELECT isDone  FROM Seances WHERE seanceID = ?");
                $stmt1->bind_param("s", $seanceID);
                $stmt1->execute();
                $stmt1->store_result();
                
                if ($stmt1->num_rows > 0) {
                    $sql = "UPDATE Seances SET isDone='$value_of_prensence'  WHERE (seanceID = '$seanceID' )";
                    
                    
                    if ($conn->query($sql) === TRUE) {
                        
                        $user = array(
                            'isDone' => $value_of_prensence,
                            'idSeance' => $seanceID
                        );
                        
                        $response['error']   = false;
                        $response['message'] = 'UpdateSecces successfull';
                        $response['user']    = $user;
                    }
                    
                } else {
                    $response['error']   = false;
                    $response['message'] = "the row not update";
                }
            }
            mysqli_close($conn);
            
            break;
        
        
        case 'listTasksMonitor':
            if (isTheseParametersAvailable(array(
                'id_current_monitor',
                'startDate'
            ))) {
                $id_current_monitor = $_POST["id_current_monitor"];
                $startDate          = $_POST["startDate"];
                $sql1               = "SELECT   title, startDate, 	durationMinut, taskID  FROM Tasks WHERE user_Fk = $id_current_monitor and  startDate like '$startDate%'";
                $result             = $conn->query($sql1);
                $clients            = array();
                
                if ($result->num_rows > 0) {
                    
                    while ($row = mysqli_fetch_array($result)) {
                        array_push($clients, array(
                            "titre" => $row[0],
                            "time" => $row[1],
                            "durationMinut" => $row[2],
                            "taskID" => $row[3]
                        ));
                        
                    }
                }
                $response['Tasks'] = $clients;
            }
            mysqli_close($conn);
            
            break;
        case 'listerSeanceCltCalendar':
            if (isTheseParametersAvailable(array(
                'current_date'
            ))) {
                $current_date   = $_POST["current_date"];
                $sql1           = "SELECT   seanceID, seanceGrpID, isDone, clientID, startDate,durationMinut  FROM Seances WHERE   startDate LIKE '$current_date%'";
                $result         = $conn->query($sql1);
                $isDone         = array();
                $seanceIDs      = array();
                $group          = array();
                $clientIDs      = array();
                $startDates     = array();
                $durationMinuts = array();
                if ($result->num_rows > 0) {
                    
                    while ($row = mysqli_fetch_array($result)) {
                        $isDone[]         = $row[2];
                        $group[]          = $row[1];
                        $clientIDs[]      = $row[3];
                        $startDates[]     = $row[4];
                        $durationMinuts[] = $row[5];
                        $seanceIDs[]      = $row[0];
                    }
                }
                
                $clients = array();
                for ($i = 0; $i < count($clientIDs); $i++) {
                    
                    $sql     = "SELECT *  FROM Clients WHERE clientID= '$clientIDs[$i]' ";
                    $result1 = $conn->query($sql);
                    if ($result1->num_rows > 0) {
                        
                        while ($row = mysqli_fetch_array($result1)) {
                            array_push($clients, array(
                                "clientID" => $row[0],
                                "clientEmail" => $row[11],
                                "passwd" => $row[12],
                                "clientPhone" => $row[13],
                                "fName" => $row[2],
                                "lName" => $row[3],
                                "isDone" => $isDone[$i],
                                "startDate" => $startDates[$i],
                                "durationMinut" => $durationMinuts[$i],
                                "seanceID" => $seanceIDs[$i]
                            ));
                            
                        }
                    }
                }
                
                $response['clients'] = $clients;
            }
            mysqli_close($conn);
            
            break;
        
        default:
            $response['error']   = true;
            $response['message'] = 'Invalid Operation Called';
    }
    
} else {
    $response['error']   = true;
    $response['message'] = 'Invalid API Call';
}


echo json_encode($response);

function isTheseParametersAvailable($params)
{
    
    foreach ($params as $param) {
        
        if (!isset($_POST[$param])) {
            
            return false;
        }
    }
    
    return true;
}
?>