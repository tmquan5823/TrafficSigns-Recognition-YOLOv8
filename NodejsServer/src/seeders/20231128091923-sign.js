'use strict';

/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.bulkInsert('Signs',
      [
        {
          code: "DP.135",
          content: "Sign indicating the end of all prohibitions",
          url: "https://i.imgur.com/eyDKz4u.png"
        },
        {
          code: "P.102",
          content: "No entry sign",
          url: "https://i.imgur.com/w6lRRQo.png"
        },
        {
          code: "P.103a",
          content: "No entry for cars",
          url: "https://i.imgur.com/FfqCzSS.png"
        },
        {
          code: "P.103b",
          content: "No left turn for cars",
          url: "https://i.imgur.com/OqHHGSo.png",
        },
        {
          code: "P.103c",
          content: "No right turn for cars",
          url: "https://i.imgur.com/0uo9s8M.png",
        },
        {
          code: "P.104",
          content: "No entry for motorcycles",
          url: "https://i.imgur.com/aLX7y2E.png"
        },
        {
          code: "P.106a",
          content: "No entry for trucks",
          url: "https://i.imgur.com/UBXTZsv.png"
        },
        {
          code: "P.106b",
          content: "No entry for trucks with a weight limit exceeding the specified limit",
          url: "https://i.imgur.com/wVPEAv8.png"
        },
        {
          code: "P.107a",
          content: "No entry for buses",
          url: "https://i.imgur.com/TRXY7Fs.png"
        },
        {
          code: "P.112",
          content: "No pedestrians",
          url: "https://i.imgur.com/b9BoUZE.png"
        },
        {
          code: "P.115",
          content: "Weight limit restriction",
          url: "https://i.imgur.com/ZlF7chN.png"
        },
        {
          code: "P.117",
          content: "Height limit restriction",
          url: "https://i.imgur.com/pkNXyfO.png"
        },
        {
          code: "P.123a",
          content: "No left turn",
          url: "https://i.imgur.com/T0f6SOI.png"
        },
        {
          code: "P.123b",
          content: "No right turn",
          url: "https://i.imgur.com/4uGpZ5c.png"
        },
        {
          code: "P.124a",
          content: "No U-turn",
          url: "https://i.imgur.com/AqAfHvV.png"
        },
        {
          code: "P.124b",
          content: "No U-turn for cars",
          url: "https://i.imgur.com/X2gJeCb.png"
        },
        {
          code: "P.124c",
          content: "No left or U-turn",
          url: "https://i.imgur.com/6b60Hwe.png"
        },
        {
          code: "P.125",
          content: "No overtaking",
          url: "https://i.imgur.com/2VAnCHQ.png"
        },
        {
          code: "P.127",
          content: "Speed limit",
          url: "https://i.imgur.com/R94rsMz.png"
        },
        {
          code: "P.128",
          content: "No horn usage",
          url: "https://i.imgur.com/NCYR8uB.png"
        },
        {
          code: "P.130",
          content: "No stopping and parking",
          url: "https://i.imgur.com/FZkJWq5.png"
        },
        {
          code: "P.131a",
          content: "No parking",
          url: "https://i.imgur.com/um7ATk0.png"
        },
        {
          code: "P.137",
          content: "No left or right turn",
          url: "https://i.imgur.com/mB8UCJs.png"
        },
        {
          code: "R.301c",
          content: "Only left turn allowed",
          url: "https://i.imgur.com/nyE9ydO.png"
        },
        {
          code: "R.301d",
          content: "Only right turn allowed",
          url: "https://i.imgur.com/zyDVtEq.png"
        },
        {
          code: "R.301e",
          content: "Only left turn allowed",
          url: "https://i.imgur.com/UxTz01d.png"
        },
        {
          code: "R.302a",
          content: "Right direction for obstacle avoidance",
          url: "https://i.imgur.com/Bn2CUIf.png"
        },
        {
          code: "R.302b",
          content: "Left direction for obstacle avoidance",
          url: "https://i.imgur.com/CY6y4kx.png"
        },
        {
          code: "R.303",
          content: "Intersection running in a roundabout",
          url: "https://i.imgur.com/4OVsSfl.png"
        },
        {
          code: "R.407a",
          content: "One-way street",
          url: "https://i.imgur.com/lBV2wgi.png"
        },
        {
          code: "R.409",
          content: "U-turn spot",
          url: "https://i.imgur.com/JtLCIOG.png"
        },
        {
          code: "R.425",
          content: "Hospital",
          url: "https://i.imgur.com/cL7lBLt.png"
        },
        {
          code: "R.434",
          content: "Bus station",
          url: "https://i.imgur.com/CTZBr5O.png"
        },
        {
          code: "W.201a",
          content: "Sharp left turn ahead",
          url: "https://i.imgur.com/WYkrtnK.png"
        },
        {
          code: "W.201b",
          content: "Sharp right turn ahead",
          url: "https://i.imgur.com/Ok6jIIf.png"
        },
        {
          code: "W.202a",
          content: "Multiple consecutive sharp turns, first to the left",
          url: "https://i.imgur.com/ppHSLgJ.png"
        },
        {
          code: "W.202b",
          content: "Multiple consecutive sharp turns, first to the right",
          url: "https://i.imgur.com/ZBsopja.png"
        },
        {
          code: "W.203b",
          content: "Road narrows to the left",
          url: "https://i.imgur.com/if8Rr2c.png"
        },
        {
          code: "W.203c",
          content: "Road narrows to the right",
          url: "https://i.imgur.com/ML7CC83.png"
        },
        {
          code: "W.205a",
          content: "Crossroads",
          url: "https://i.imgur.com/02n3Dm5.png"
        },
        {
          code: "W.205b",
          content: "Crossroads",
          url: "https://i.imgur.com/DZxxh1Q.png"
        },
        {
          code: "W.205d",
          content: "Crossroads",
          url: "https://i.imgur.com/rpabQkf.png"
        },
        {
          code: "W.207a",
          content: "Crossroads with no priority",
          url: "https://i.imgur.com/NpWf8R3.png"
        },
        {
          code: "W.207b",
          content: "Crossroads with no priority",
          url: "https://i.imgur.com/I9aky8n.png"
        },
        {
          code: "W.207c",
          content: "Crossroads with no priority",
          url: "https://i.imgur.com/BNXlBMl.png"
        },
        {
          code: "W.208",
          content: "Crossroads with priority",
          url: "https://i.imgur.com/tcScj51.png"
        },
        {
          code: "W.209",
          content: "Traffic light-controlled intersection",
          url: "https://i.imgur.com/UO1V4lX.png"
        },
        {
          code: "W.210",
          content: "Railway crossing with barriers",
          url: "https://i.imgur.com/X01UzyM.png"
        },
        {
          code: "W.219",
          content: "Downhill slope ahead",
          url: "https://i.imgur.com/jrVEVLz.png"
        },
        {
          code: "W.221b",
          content: "Road with a simulated reduction in speed",
          url: "https://i.imgur.com/3lqM29z.png"
        },
        {
          code: "W.224",
          content: "Pedestrian crossing",
          url: "https://i.imgur.com/5pqM2kM.png"
        },
        {
          code: "W.225",
          content: "Children present",
          url: "https://i.imgur.com/mg5aTKx.png"
        },
        {
          code: "W.227",
          content: "Construction site",
          url: "https://i.imgur.com/vO9LFlX.png"
        },
        {
          code: "W.233",
          content: "Other danger",
          url: "https://i.imgur.com/trC1aNz.png"
        },
        {
          code: "W.235",
          content: "Dual carriageway",
          url: "https://i.imgur.com/FymipbT.png"
        },
        {
          code: "W.245a",
          content: "Slow down",
          url: "https://i.imgur.com/ldAO9Bo.png"
        }
      ], {});
  },

  async down(queryInterface, Sequelize) {
    /**
     * Add commands to revert seed here.
     *
     * Example:
     * await queryInterface.bulkDelete('People', null, {});
     */
  }
};
