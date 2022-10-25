const PI = Math.PI

exports.area = (r) => PI*r*r;
exports.circumference = (r) => 2*PI*r;

exports.area1 = function(p1, p2) {
    return PI*p1*p2;
}


// exports = {
//    area : area,
//    circumference: circumference,
//    area1 : area1
// }

// exports.area = function(r) {
//     return PI*r*r
// }