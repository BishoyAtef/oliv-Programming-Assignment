import CustomNode from "./CustomNode";

const CustomTree = ({ data }) => {
  if (!data) {
    return (
      <div className="text-gray-500 italic text-center mt-4">
        No expense tree data available.
      </div>
    );
  }

  return (
    <div className="bg-white p-6 rounded-xl shadow-md border border-gray-200 max-w-4xl mx-auto mt-6">
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Expense Tree Breakdown</h2>
      <CustomNode node={data} />
    </div>
  );
};

export default CustomTree;

