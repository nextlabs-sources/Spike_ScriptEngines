"use strict";

const path = require('path');
const Sandbox = require('ghost-sandbox');
const good_module = path.join(__dirname, './good_module');
const bad_module = path.join(__dirname, './bad_module');

const plugin = path.join(__dirname, './plugin');



      let sandbox = new Sandbox({
        blacklist: ['http']
      });

      try {
        let sandboxed = sandbox.loadWidget(plugin);

        //done(new Error('Bad module was loaded'));
      } catch (error) {
        console.log(good_module.toString)
        console.log("Cannot load this module");
        //error.should.be.instanceOf(Error);
        //done();
      }





    //   let sandbox = new Sandbox({
    //     whitelist: ['net']
    //   });

    //   try {
    //     let sandboxed = sandbox.loadWidget(bad_module);

    //     done(new Error('Bad module was loaded'));
    //   } catch (error) {
    //  //   error.should.be.instanceOf(Error);
    //     done();
    //   }




    //   let sandbox = new Sandbox({
    //     whitelist: ['http'],
    //     blacklist: ['http']
    //   });

    //   let sandboxed = sandbox.loadWidget(good_module);

    //   //sandboxed.name.should.be.eql('Good module');
    //   done();



