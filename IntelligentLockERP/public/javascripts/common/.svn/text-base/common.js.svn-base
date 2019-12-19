/****************************************************************************
 * DESC       ：公用函数变量定义
 * AUTHOR     ：lixiaoming
 * CREATEDATE ：2012-12-20
 * MODIFYLIST ：   Name       Date            Reason/Contents
 *          ------------------------------------------------------
 *              lixiaoming   2012-12-20       计算两个日期的差,返回差的月数(M)或天数(D)
 *              
 *          
 *          
 *          
 *          
 *          ---后续方法延续
/************************************************************************************/

//计算两个日期的差,返回差的月数(M)或天数(D)
function dateDiff(dateStart,dateEnd,MD)
{
  var i;
  if(MD=="D") //按天计算差
  {
    var endTm   = dateEnd.getTime();
    var startTm = dateStart.getTime();
    var diffDay = (endTm - startTm)/86400000 + 1;

    return diffDay;
  }
  else //按月计算差
  {
    var endD   = dateEnd.getDate();
    var endM   = dateEnd.getMonth();
    var endY   = dateEnd.getFullYear();
    var startD = dateStart.getDate();
    var startM = dateStart.getMonth();
    var startY = dateStart.getFullYear();

    if(endD>=startD) //跟终端版fcalc_month函数统一，endD>startD时才加1
    {
      return (endY-startY)*12 + (endM-startM) + 1;
    }
    else
    {
      return (endY-startY)*12 + (endM-startM);
    }
  }
}