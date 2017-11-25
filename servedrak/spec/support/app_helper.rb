module Servedrak
  module AppHelper
    def app
      app_tree.server.rack.main
    end

    def app_tree
      @app_tree ||= Servedrak::AppTree.new
    end
  end
end
