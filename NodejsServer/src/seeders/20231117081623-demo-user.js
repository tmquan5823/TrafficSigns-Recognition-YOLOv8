'use strict';
const hash = require('../helpers/password-encrypter')
const path = require('path')
const fs = require('fs')
const staticPath = path.join(__dirname, '../public/avatar/default.png');
const defaultAvatar = fs.readFileSync(staticPath)

/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    /**
     * Add seed commands here.
     *
     * Example:
     * await queryInterface.bulkInsert('People', [{
     *   name: 'John Doe',
     *   isBetaMember: false
     * }], {});
    */
    await queryInterface.bulkInsert('Users',
      [{
        username: 'hovanthao',
        password: hash.hash('123456'),
        name: 'Hồ Văn Thảo',
        phone: '0369276372',
        email: 'hovanthao0611cs@gmail.com',
        address: 'Gia Lai',
        avatar: defaultAvatar
      },
      {
        username: 'voviettruong141003@gmail.com',
        password: hash.hash('123456'),
        name: 'Võ Viết Trường',
        phone: '0857937360',
        email: 'voviettruong141003@gmail.com',
        address: 'Quảng Nam',
        avatar: defaultAvatar
      },
      {
        username: 'hoangnguyennhatminh',
        password: hash.hash('123456'),
        name: 'Hoàng Nguyễn Nhật Minh',
        phone: '0325006036',
        email: 'hoangnguyennhatminh2003ht@gmail.com',
        address: 'Hà Tĩnh',
        avatar: defaultAvatar
      },
      {
        username: 'tranminhquan',
        password: hash.hash('123456'),
        name: 'Trần Minh Quân',
        phone: '0948628477',
        email: 'tmquan0948628477@gmail.com',
        address: 'Quảng Trị',
        avatar: defaultAvatar
      }
      ],
      {}
    );
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
