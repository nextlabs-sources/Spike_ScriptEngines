// const circle=require('./circle.js');
// var area = circle.area(4);
// console.log('The area of a circle of radius 4 is '+area);

console.log("Hello world 1");

exports.fun1 = function() {
    //return p1*p2;
    console.log("OMG You just called me");
}

exports.fun2 = function() {
    //return p1*p2;
    console.log("OMG You just called me 2");
}

exports.fun2 = function() {
    console.log("test here");
    return "Amila Silva";
}

exports.test = function(a, b, successHandler){
    //setTimeout(function() {
      successHandler(a + b);  
    //}, 1);
    //successHandler(a + b);
}