import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author Omar Takruri
 *
 */
public final class XMLTreeExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        NaturalNumber evaluated = new NaturalNumber1L();

        // Addition (+)
        if (exp.label().equals("plus")) {
            NaturalNumber left = evaluate(exp.child(0));
            NaturalNumber right = evaluate(exp.child(1));
            evaluated.add(left);
            evaluated.add(right);
        }

        // Subtraction (-)
        if (exp.label().equals("minus")) {
            NaturalNumber left = evaluate(exp.child(0));
            NaturalNumber right = evaluate(exp.child(1));
            if (left.compareTo(right) >= 0) {
                evaluated.subtract(right);
            } else {
                Reporter.fatalErrorToConsole(
                        "Negative result in Natural Numbers");
            }
        }

        // Multiplication (*)
        if (exp.label().equals("times")) {
            NaturalNumber left = evaluate(exp.child(0));
            NaturalNumber right = evaluate(exp.child(1));
            evaluated.multiply(left);
            evaluated.multiply(right);
        }

        // Division (/)
        if (exp.label().equals("divide")) {
            NaturalNumber numerator = evaluate(exp.child(0));
            NaturalNumber denominator = evaluate(exp.child(1));

            // Check if we are dividing by zero
            if (denominator.isZero()) {
                Reporter.fatalErrorToConsole("Cannot divide by zero");
            }

            evaluated.copyFrom(numerator);
            evaluated.divide(denominator);
        }

        // Number (base case)
        if (exp.label().equals("number")) {
            String value = exp.attributeValue("value");
            evaluated = new NaturalNumber1L(value);
        }

        return evaluated;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
