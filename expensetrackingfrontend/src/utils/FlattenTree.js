const flattenTree = (node) => {
  if (!node.children || node.children.length === 0) {
    return [{ name: node.tag, value: node.sum, amount: node.amount || 0, children: [] }];
  }
  const childrenData = node.children.map((child) => ({
    name: child.tag,
    value: child.sum,
    children: child.children || [],
    amount: child.amount || 0
  }));
  if (node.amount && node.amount > 0) {
    childrenData.push({
      name: "#other",
      value: node.amount,
      amount: node.amount,
      children: []
    });
  }
  return childrenData;
};

export default flattenTree;