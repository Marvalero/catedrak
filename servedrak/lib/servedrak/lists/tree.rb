require_relative 'actions/tree'
require_relative 'collection'

module Servedrak
  module Lists
    class Tree
      include Treefier::Base

      def collection
        @collection ||= Servedrak::Lists::Collection.new(self)
      end

      def actions
        @actions ||= Servedrak::Lists::Actions::Tree.new(self)
      end
    end
  end
end
