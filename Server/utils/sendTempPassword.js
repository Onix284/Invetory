const nodemailer = require('nodemailer');

//Function to send email
module.exports = function sendTempPassword(email, password){

    const transporter = nodemailer.createTransport({
        service : "gmail",
        auth : {
            user : "omfreelancer281@gmail.com",
            pass : "oxxn xtpf qthx hxhr"
        }
    });

    const mailOptions = {
        from : "omfreelancer281@gmail.com",
        to : email,
        subject : "Temporary Password",
        text : `Your temporary password is: ${password}. Please change it after logging in.`
    };

    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            console.error('Error sending email:', error);
            return;
        }
        console.log('Email sent:', info.response);
    });
};