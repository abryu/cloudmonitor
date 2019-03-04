const url = 'http://localhost:8080';

var userId = '1';

var typeBasic ="BASIC";

function getccId(){
    return userId;
}

function setccId(id){
    userId = id;
}

function checkValue(node){

    let input = node.value;
    if(input!=null&&input!=""&&!isNaN(input)){

    }else{
        node.value=0;
    }

}

function checkString(node){
    let input = node.value;
    if(isValidString(input)){

    }else{
        alert("name contains invalid parttern");
    }

}
// user login
function login(){

    let _name = document.getElementById("userName").value;
    let _password = document.getElementById("password").value;
    let requestUrl = url+'/consumer?username='+_name+'&password='+_password;
    // let data = {
    //     'name': _name,
    //     'password': _password,
       
    // }

    let request = new Request(requestUrl, {
        method: 'GET',
        // body: JSON.stringify(data),
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8',
            "Accept-Charset":"charset=utf-8"
        })
    });

    fetch(request)
    .then(resp => {
        return resp.json();
        
    }).then(json=>{
        console.log(json);
        let user = json[0];
        setccId(user.ccId);
        console.log('user login, id='+getccId());
    })
    .catch(err => {

    });
}

//entry of generate VM list function
function refreshVMList(){
    
    let requestUrl = url+'/consumer/'+getccId()+'/all';

    let request = new Request(requestUrl, {
        method: 'GET',
        headers: new Headers({"Accept-Charset":"charset=utf-8"})
    });
    
    fetch(request)
    .then(response=>{
        return response.json();
      })
      .then(json=> {
        console.log(json);
        
        for(i = 0,len=json.length; i< len; i++){
            
            let VM = json[i];
            let id = VM.vmId;
            if(document.getElementById(id)){
                existNode = document.getElementById(id);
                updateTableNode(VM,existNode);
            }else if(VM.eventType=="CREATED"||VM.eventType=="RUNNING"){
                createVMTableNode(VM);
            }

            // console.log(fruit);
            
        }


      }).catch(err => {
        console.log(err);
    });

}

//create VM List nodes, generate action buttons
function createVMTableNode(jsonElement){
    let id = jsonElement.vmId;
    let name = jsonElement.vmId;
    let type = jsonElement.vmType;
    let _status = jsonElement.eventType;

    let status = '';

    if(_status=="CREATED"){
        status='stop';
    }else if(_status="RUNNING"){
        status='running';
    }else if(_status='DELETED'){
        return;
    }
    
    console.log(id,name,type,status);
    
    let vmTable = document.getElementById('vmTable');
    
    let trNode=document.createElement("tr");
    trNode.setAttribute("id",id);
    
    let tdNodeName = document.createElement("td");
    tdNodeName.setAttribute("class","name");

    let tdNodeType = document.createElement("td");
    tdNodeType.setAttribute("class","type");

    let tdNodeStatus = document.createElement("td");
    tdNodeStatus.setAttribute("class","status");

    let tdNodeStart = document.createElement("td");
    tdNodeStart.setAttribute("class","start");
    
    let tdNodeStop = document.createElement("td");
    tdNodeStop.setAttribute("class","stop");

    let tdNodeUpgrade = document.createElement("td");
    tdNodeUpgrade.setAttribute("class","upgrade");

    let tdNodeDowngrade = document.createElement("td");
    tdNodeDowngrade.setAttribute("class","downgrade");

    let tdNodeDelete = document.createElement("td");
    tdNodeDelete.setAttribute("class","delete");

    let tdNodeDetail = document.createElement("td");
    tdNodeDetail.setAttribute("class","detail");


    tdNodeName.innerHTML = name;
    tdNodeType.innerHTML = type;
    tdNodeStatus.innerHTML = status;

    let startButton = document.createElement("input");
    startButton.setAttribute("type","button");
    startButton.setAttribute("value","start");
    startButton.setAttribute("onclick","startVM(this.parentNode)");
   
    let stopButton = document.createElement("input");
    stopButton.setAttribute("type","button");
    stopButton.setAttribute("value","stop");
    stopButton.setAttribute("onclick","stopVM(this.parentNode)");

    let upgradeButton = document.createElement("input");
    upgradeButton.setAttribute("type","button");
    upgradeButton.setAttribute("value","upgrade");
    upgradeButton.setAttribute("onclick","upgradeVM(this.parentNode)");

    let downgradeButton = document.createElement("input");
    downgradeButton.setAttribute("type","button");
    downgradeButton.setAttribute("value","downgrade");
    downgradeButton.setAttribute("onclick","downgradeVM(this.parentNode)");

    let deleteButton = document.createElement("input");
    deleteButton.setAttribute("type","button");
    deleteButton.setAttribute("value","delete");
    deleteButton.setAttribute("onclick","deleteVM(this.parentNode)");

    tdNodeDetail.innerHTML=jsonElement.accumulatedBalance;

    // let detailButton = document.createElement("input");
    // detailButton.setAttribute("type","button");
    // detailButton.setAttribute("value","show detail");
    // detailButton.setAttribute("onclick","detailNode(this.parentNode)");

    tdNodeStart.appendChild(startButton);
    tdNodeStop.appendChild(stopButton);
    tdNodeUpgrade.appendChild(upgradeButton);
    tdNodeDowngrade.appendChild(downgradeButton);
    tdNodeDelete.appendChild(deleteButton);
    // tdNodeDetail.appendChild(detailButton);

    trNode.append(tdNodeName);
    trNode.append(tdNodeType);
    trNode.append(tdNodeStatus);
    trNode.append(tdNodeStart);
    trNode.append(tdNodeStop);
    trNode.append(tdNodeUpgrade);
    trNode.append(tdNodeDowngrade);
    trNode.append(tdNodeDelete);
    trNode.appendChild(tdNodeDetail);

    vmTable.appendChild(trNode);


}
//VM table data update
function updateTableNode(jsonElement,node){
    
    let typeNode = node.getElementsByClassName("type")[0];
    let statusNode = node.getElementsByClassName("status")[0];
    let tdNodeDetail = node.getElementsByClassName("detail")[0];
   
    let type = jsonElement.vmType;
    let _status = jsonElement.eventType;
    let balance = jsonElement.accumulatedBalance;

    let status = '';

    if(_status=="CREATED"){
        status='stop';
    }else if(_status="RUNNING"){
        status='running';
    }else if(_status='DELETED'){
        return;
    }
    
    typeNode.innerHTML = type;
    statusNode.innerHTML = status;
    tdNodeDetail.innerHTML=balance;
}

// VM tables buttons functions implements

function startVM(node){
    let vmid = node.parentNode.id;
    
    let requestUrl = url+'/vm/start?ccId='+getccId()+'&&vmId='+vmid;
    updateVMRequest(node,requestUrl);
}

function stopVM(node){
    
    let vmid = node.parentNode.id;
    
    let requestUrl = url+'/vm/stop?ccId='+getccId()+'&&vmId='+vmid;
    updateVMRequest(node,requestUrl);
    
}
function upgradeVM(node){
    let vmid = node.parentNode.id;
    let typeNode = node.parentNode.getElementsByClassName("type")[0];
    let type = typeNode.innerHTML;
    if(!canUpgrade(type)){
        alert("cannot upgrade");
        return;
    }
    
    let requestUrl = url+'/vm/upgrade?ccId='+getccId()+'&&vmId='+vmid;
    updateVMRequest(node,requestUrl);
}
function downgradeVM(node){
    let vmid = node.parentNode.id;
    let typeNode = node.parentNode.getElementsByClassName("type")[0];
    let type = typeNode.innerHTML;
    
    if(!canDowngrade(type)){
        alert("cannot downgrade");
        return;
    }

    let requestUrl = url+'/vm/downgrade?ccId='+getccId()+'&&vmId='+vmid;
    updateVMRequest(node,requestUrl);
}


function detailNode(node){
    
}

//VM status update
function updateVMRequest(node,requestUrl){


    
    let request = new Request(requestUrl, {
        method: 'POST',
        // body: JSON.stringify(data),
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8',
            "Accept-Charset":"charset=utf-8"
        })
    });

    fetch(request)
        .then(resp => {
            
            if(Number(resp.status)==200){
                console.log("updated");
                
                
            }else{
                alert("update failed",resp);
            }
        });
    
        refreshVMList();
}


function deleteVM(node){
    
    let elementTable = document.getElementById('vmTable');
    let vmid = node.parentNode.id;
    
    let requestUrl = url+'/vm/delete?ccId='+getccId()+'&&vmId='+vmid;
 
    let request = new Request(requestUrl, {
        method: 'DELETE',
        
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8',
            "Accept-Charset":"charset=utf-8"
        })
    });

    fetch(request)
    .then(resp => {
        if(Number(resp.status)==200){
            console.log("deleted");

            elementTable.removeChild(node.parentNode);
        }else{
            alert("delete failed",resp);
        }

    })
    .catch(err => {
        alert("delete failed",err);
    });
    refreshVMList()
}

function createVM(){
    let selectorNode = document.getElementById("createVMSelector");
    let vmType = selectorNode.value;
    console.log(selectorNode.value);

    let requestUrl = url+'/vm/create?ccId='+getccId()+'&&vmType='+vmType;
    
    let request = new Request(requestUrl, {
        method: 'POST',
        // body: JSON.stringify(data),
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8',
            "Accept-Charset":"charset=utf-8"
        })
    });

    fetch(request)
        .then(resp => {
            
            if(Number(resp.status)==200){
                console.log("create successful");
                
                
            }else{
                alert("create failed",resp);
            }
        });
    
    refreshVMList();
}

function isValidString(value){
    let pattern ='.,</>?:;-=!#$%&|{}[]()\'\"\\';
    for(let i = 0, len = value.length; i < len; i++) {  
        if(pattern.indexOf(value[i])){
            // console.log(pattern[i]);
            return false;
        }
    }
    return true;
}

function isValidNumber(value){
    if(value!=null&&value!=""&&!isNaN(value)){
        return true;
    }else{
        return false;
    }
}

function canUpgrade(type){
    if(type!='ULTRA'&&type=="BASIC"||type=="LARGE"){
        return true;
    }else{
        return false;
    }
}

function canDowngrade(type){
    if(type!='BASIC'&&type=="ULTRA"||type=="LARGE"){
        return true;
    }else{
        return false;
    }
}
// refresh();
// setInterval(refresh, 2000);
