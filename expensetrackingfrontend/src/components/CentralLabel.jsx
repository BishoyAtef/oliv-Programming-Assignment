
const CenterLabel = ({ viewBox, total }) => {
  const { cx, cy } = viewBox;
  const value = `${total/1000}K`
  return (
    <g>
      <text  
        x={cx - 75} 
        y={cy - 30} 
        textAnchor="start" 
        fill="gray"
        fontSize="18"
      >
        EGP
      </text>
      <text 
        x={cx - 75}
        y={cy + 25}
        textAnchor="start"
        fill="black"
        fontSize="48"
        fontWeight="bold"
      >
        {value}
      </text>
    </g>
  );
};

export default CenterLabel;