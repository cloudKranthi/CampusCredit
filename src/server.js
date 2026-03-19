const {dripQueue}=require('./queue');
const app=require('express');
app.use("/api/adddrip/valut",
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
  res.send("Money sended succesfully")
});
app.listen(3000,()=>{
  console.log('Node server lisetining')
});
module.exports=handlerequest;