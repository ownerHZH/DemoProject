for(var i = 0; i < 24; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});
u21.tabIndex = 0;

u21.style.cursor = 'pointer';
$axure.eventManager.click('u21', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('通知详情.html');

}
});
gv_vAlignTable['u21'] = 'top';gv_vAlignTable['u16'] = 'top';gv_vAlignTable['u12'] = 'center';gv_vAlignTable['u23'] = 'top';gv_vAlignTable['u4'] = 'top';gv_vAlignTable['u8'] = 'center';gv_vAlignTable['u10'] = 'center';gv_vAlignTable['u17'] = 'top';gv_vAlignTable['u22'] = 'top';document.getElementById('u5_img').tabIndex = 0;

u5.style.cursor = 'pointer';
$axure.eventManager.click('u5', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('首页.html');

}
});
gv_vAlignTable['u1'] = 'center';
$axure.eventManager.mouseover('u9', function(e) {
if (!IsTrueMouseOver('u9',e)) return;
if (true) {

	SetPanelState('u13', 'pd0u13','none','',500,'none','',500);

}
});
gv_vAlignTable['u20'] = 'center';gv_vAlignTable['u15'] = 'center';gv_vAlignTable['u6'] = 'center';
$axure.eventManager.mouseover('u11', function(e) {
if (!IsTrueMouseOver('u11',e)) return;
if (true) {

	SetPanelState('u13', 'pd1u13','none','',500,'none','',500);

}
});
gv_vAlignTable['u18'] = 'top';