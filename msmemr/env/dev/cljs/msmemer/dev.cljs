(ns ^:figwheel-no-load msmemer.dev
  (:require
    [msmemer.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
