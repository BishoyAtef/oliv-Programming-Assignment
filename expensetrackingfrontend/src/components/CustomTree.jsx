const ExpenseNode = ({ node, level = 0 }) => {
  const hasChildren = node.children && node.children.length > 0;
  const hue = (level * 60) % 360;
  const bgColor = `hsl(${hue}, 80%, 85%)`;
  return (
    <div style={{ backgroundColor: bgColor }} className="pl-4 border-l border-gray-300 rounded-md my-2">
      <div className="py-2 px-3 rounded-md">
        <span className="font-semibold text-gray-900">{node.tag.slice(1)}</span> — 
        <span className="ml-1 text-gray-800">{node.sum} EGP</span>
        {node.amount > 0 && (
          <span className="ml-2 text-sm text-gray-600">(other: {node.sum - node.amount} EGP)</span>
        )}
      </div>

      {hasChildren && (
        <div className="mt-1 space-y-1">
          {node.children.map((child, idx) => (
            <ExpenseNode key={idx} node={child} level={level + 1} />
          ))}
        </div>
      )}
    </div>
  );
};

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
      <ExpenseNode node={data} />
    </div>
  );
};

export default CustomTree;

