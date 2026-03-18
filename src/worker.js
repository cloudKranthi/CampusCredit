const {Worker}=require('bullmq');
const {pool}=require('./db');

const {dripQueue,connection}=require('./queue');
const worker=new Worker("drip-queue",async(job)=>{
    const{parentphoneNumber,studentphoneNumber,dailyLimit,balanceAmount}=job.data;

   const client=await pool.connect();
   try{
    await client.query('BEGIN')
    await client.query('UPDATE valUts SET total_balance=total_balance-$1 WHERE parent_phone_number=$2 AND student_phone_number=$3',
        [dailyLimit,parentphoneNumber,studentphoneNumber]
    );

   await client.query('UPDATE purses SET balance= balance + $1::numeric WHERE phoneNumber=$2 ',[dailyLimit,studentphoneNumber]);
   await client.query('COMMIT');
    
    let nextbalanceAmount=balanceAmount-dailyLimit;
    
    if(nextbalanceAmount>0){
        await dripQueue.add("drip-payouts",{
        parentphoneNumber,
        studentphoneNumber,
        dailyLimit,
        nextbalanceAmount
    },{delay:24*60*60*1000});
}else{
    console.log("not sufficient amount is present in the account");
}}
catch(err){
    client.query('ROLLBACK');
    throw err;
}
finally{
    client.release();
}},{connection}
);
