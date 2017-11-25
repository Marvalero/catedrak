require 'roo'

module Servedrak
  module Lists
    class Collection
      LISTS_FILE = './res/lists.xlsx'
      # Expected format:
      # id | last_name | name | list1 | list2 | ... | listN | paid

      def initialize(_configurator, filters: {})
        self.filters = filters
      end

      def where(filters)
        return self unless filters['name']
        self.class.new(nil, filters: { name: filters['name'] })
      end

      def to_a
        if filters[:name]
          return lists.select { |l| l[:name].downcase == filters[:name].downcase }.to_a
        end
        return lists.to_a
      end

      private
      attr_writer :filters

      def filters
        @filters ||= {}
      end

      def lists
        @lists ||= lists_names.each_with_index.map do |list_name, index|
          {
            name: list_name,
            items: users_for_list(index + 4)
          }
        end
      end

      def lists_names
        @lists_names ||= file_content.row(1)[3...-1].map(&:strip)
      end

      def users_for_list(index)
        file_content.column(index)[1..-1].each_with_index.map do |included, index|
          included && create_person(file_content.row(index + 2))
        end.compact
      end

      def create_person(data)
        {
          id: data[0].strip,
          last_name: data[1],
          name: data[2],
          paid: !!data[-1]
        }
      end

      def file_content
        @file_content ||= Roo::Spreadsheet.open(LISTS_FILE)
      end
    end
  end
end
