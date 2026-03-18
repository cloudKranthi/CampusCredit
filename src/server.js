const {dripQueue}=require('./queue');
 async  function handlerequest(req,res){
  const{parentphoneNumber,studentphoneNumber,dailyLimit,balanceAmount}=req.body;
  await dripQueue.add("drip-payouts",{
    parentphoneNumber,
    studentphoneNumber,
    dailyLimit,
    remainingAmount:balanceAmount-dailyLimit
  },{
    delay:24*60*60*1000
  });
  console.log('Drip initiated in redis');
}
module.exports=handlerequest;