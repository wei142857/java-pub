/**
* HTML接收参数
*/
function htmlRequest(paramName)
{
	//获取URL的字符串
	var sSource=String(window.document.location);
	var sName= paramName;  
	var sReturn="";   
	var sQUS="?";   
	var sAMP="&";   
	var sEQ="=";   
	var iPos;

	//获取sSource中的"?"，无则返回 -1
	iPos=sSource.indexOf(sQUS);
	if(iPos==-1) 
		return; 
	
	//汲取参数，从iPos位置到sSource.length-iPos的位置，
	//若iPos = -1，则：从-1 到 sSource.length+1
	var strQuery=sSource.substr(iPos,sSource.length-iPos); 

	// alert(strQuery); 
	//先全部转换为小写
	var   strLCQuery = strQuery.toLowerCase();   
	var   strLCName  = sName.toLowerCase(); 

	//从子字符串strLCQuery中查找“?”、参数名，以及“=”，即“?参数名=”
	iPos   = strLCQuery.indexOf(sQUS + strLCName + sEQ);
	//alert(iPos);
	//如果不存在
	if(iPos == -1)   
	{   
		//继续查找可能的后一个参数，即带“&参数名=”
		iPos = strLCQuery.indexOf(sAMP + strLCName + sEQ);   
	}

	//判断是否存在参数
	if(iPos != -1)
	{    
		sReturn = strQuery.substr(iPos + sName.length + 2,strQuery.length-(iPos + sName.length + 2));   
		var iPosAMP = sReturn.indexOf(sAMP);   
		if (iPosAMP == -1)
		{
			return   sReturn;   
		}
		else
		{   
			sReturn = sReturn.substr(0,iPosAMP);   
		}   
	}
	return   sReturn; 
}