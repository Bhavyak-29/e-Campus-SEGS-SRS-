function displayFooter()
{
document.write("<table width='100%'>");
document.write("  <tr valign='top'> ");
document.write("    <td valign='top' align='center' ><font face='Verdana, Arial, Helvetica,  sans-serif' size='1' color='#4B59A4'> ");
document.write("      Best viewed in Microsoft Internet Explorer 5.0+ with 800x600 resolution <br>");
document.write("      You are currently using<font color='#3333FF'> ");
var useragent = navigator.userAgent;
var bName = (useragent.indexOf('Opera') > -1) ? 'Opera' : navigator.appName;
var pos = useragent.indexOf('MSIE');
if (pos > -1) 
{
bVer = useragent.substring(pos + 5);
var pos = bVer.indexOf(';');
var bVer = bVer.substring(0,pos);
}
var pos = useragent.indexOf('Opera');
if (pos > -1)	{
bVer = useragent.substring(pos + 6);
var pos = bVer.indexOf(' ');
var bVer = bVer.substring(0, pos);
}
if (bName == 'Netscape') {
var bVer = useragent.substring(8);
var pos = bVer.indexOf(' ');
var bVer = bVer.substring(0, pos);
}
if (bName == 'Netscape' && parseInt(navigator.appVersion) >= 5) {
var pos = useragent.lastIndexOf('/');
var bVer = useragent.substring(pos + 1);
}
document.writeln(bName);
document.writeln(bVer);

document.write("      </font> in<font color='#3333FF'> ");
window.onerror=null;
monRes = window.screen.width;
monRes += 'x';
monRes += window.screen.height;
document.write(monRes);
document.write("      </font><font face='Verdana, Arial, Helvetica, sans-serif' size='1'>resolution. <br><b> Designed,");
document.write("      Developed &amp; Maintained by Internet ExchangeNext.com Ltd.</b><br>");
document.write("      </td></tr>");
document.write("  </table>");
}