package com.bartoszjanota.plugin.example

import akka.actor.Actor
import akka.actor.Actor.Receive

class ScalaClass() extends Actor{
  def printYourself(): String ={
    "This it the scala class!"
  }

  override def receive: Receive = {
    case _ =>
      println("done")
  }
}