module Peppaframe
  module BaseActions
    class Show
      def initialize(configurator)
        self.collection = configurator.collection
      end

      def call(filters)
        collection.where(filters).to_a
      end

      private
      attr_accessor :collection
    end
  end
end
