// 变量'zcw'作为 DOM window 对象的属性
var zcw = 'zhongcw';

var obj_1 = {
	'name':'eee'
}
var obj_2 = {
	'name':'ccc'
}
var obj_3 = {
	'name':'bbb'
}
var obj_4 = {
	'name':'aaa'
}
var obj_5 = {
	'name':'ddd'
}

// 创建数组
var ary = [];
ary.push('e');
ary.push('c');
ary.push('b');
ary.push('a');
ary.push('d');
alert(ary);

//ary.push(obj_1)
//ary.push(obj_2)
//ary.push(obj_3)
//ary.push(obj_4)
//ary.push(obj_5)


for(var i=0;i<ary.length;i++){
//	alert(ary[i].name)
//	alert(ary[i])
}

ary.sort();
alert(ary);
for(var i=0;i<ary.length;i++){
//	alert('sort: '+ary[i].name)
//	alert('sort: '+ary[i])
}

//ary.sort(numberorder)
//alert(ary)

function numberorder(a, b) {
//	return a.name - b.name;
	return a - b;
}
